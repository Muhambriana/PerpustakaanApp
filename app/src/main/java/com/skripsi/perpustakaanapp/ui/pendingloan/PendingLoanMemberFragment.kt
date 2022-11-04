package com.skripsi.perpustakaanapp.ui.pendingloan

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.adapter.PendingLoanAdapter
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.databinding.FragmentPendingLoanMemberBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.ui.MySnackBar
import com.skripsi.perpustakaanapp.ui.book.detailbook.DetailBookActivity
import com.skripsi.perpustakaanapp.ui.userprofile.UserProfileActivity

class PendingLoanMemberFragment : Fragment() {

    private lateinit var binding: FragmentPendingLoanMemberBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: PendingLoanMemberViewModel

    private val client=  RetrofitClient
    private val pendingLoanAdapter = PendingLoanAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPendingLoanMemberBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        firstInitialization()
        getPendingLoanData()
        onClickListener()
    }

    private fun firstInitialization() {

        sessionManager = SessionManager(requireContext())

        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
            PendingLoanMemberViewModel::class.java
        )
    }

    private fun getPendingLoanData() {
        viewModel.getAllPendingLoans(sessionManager.fetchAuthToken().toString())

        viewModel.resourcePendingLoan.observe(requireActivity()) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is MyResource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is MyResource.Success -> {
                        binding.rvPendingLoan.adapter = pendingLoanAdapter
                        binding.progressBar.visibility = View.GONE
                        pendingLoanAdapter.setPendingLoanList(resource.data)
                    }
                    is MyResource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        MyAlertDialog.showWith2Event(
                            requireContext(),
                            R.drawable.icon_cancel,
                            resource.message.toString(),
                            resources.getString(R.string.refresh),
                            resources.getString(R.string.back_to_dashboard),
                            { _,_ ->
                                getPendingLoanData()
                            },
                            { _,_ ->
                                activity?.finish()
                            }
                        )
                    }
                    is MyResource.Empty -> {
                        binding.progressBar.visibility = View.GONE
                        binding.viewEmpty.root.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun onClickListener() {
        pendingLoanAdapter.onMemberUsernameClick = { memberUsername ->
            val intent = Intent(requireContext(), UserProfileActivity::class.java)
            intent.putExtra(UserProfileActivity.USERNAME, memberUsername)
            startActivity(intent)
        }

        pendingLoanAdapter.onBookTitleClick = { bookId ->
            val intent = Intent(requireContext(), DetailBookActivity::class.java)
            intent.putExtra(DetailBookActivity.BOOK_ID, bookId)
            startActivity(intent)
        }
    }



}