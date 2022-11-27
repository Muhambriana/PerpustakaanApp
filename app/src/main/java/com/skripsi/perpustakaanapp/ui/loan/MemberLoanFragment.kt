package com.skripsi.perpustakaanapp.ui.loan

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.adapter.LoanHistoryAdapter
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.databinding.FragmentMemberLoanBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.ui.book.detailbook.DetailBookActivity
import com.skripsi.perpustakaanapp.ui.book.detailloanhistory.DetailLoanHistoryActivity
import com.skripsi.perpustakaanapp.ui.userprofile.UserProfileActivity

class MemberLoanFragment : Fragment() {

    private lateinit var binding: FragmentMemberLoanBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: MemberLoanViewModel

    private val client = RetrofitClient
    private val loanHistoryAdapter = LoanHistoryAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMemberLoanBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        firstInitialization()
        getLoanData()
        onClickListener()
    }

    private fun firstInitialization() {
        sessionManager = SessionManager(requireContext())
        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
            MemberLoanViewModel::class.java
        )
    }

    private fun onClickListener() {
        loanHistoryAdapter.onItemClick =  {
            val intent = Intent(requireContext(), DetailLoanHistoryActivity::class.java)
            intent.putExtra(DetailLoanHistoryActivity.EXTRA_DATA, it)
            startActivity(intent)
        }
        loanHistoryAdapter.onBookTitleClick = { bookId ->
            val intent = Intent(requireContext(), DetailBookActivity::class.java)
            intent.putExtra(DetailBookActivity.BOOK_ID, bookId)
            startActivity(intent)
        }
        loanHistoryAdapter.onOfficerUsernameClick = { officerUsername ->
            val intent = Intent(requireContext(), UserProfileActivity::class.java)
            intent.putExtra(UserProfileActivity.USERNAME, officerUsername)
            startActivity(intent)
        }
    }

    private fun getLoanData() {
        // To know which menu call this, and set with the right data
        setRequest()

        viewModel.resourceLoan.observe(requireActivity()) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is MyResource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is MyResource.Success -> {
                        binding.rvHistoryLoan.adapter = loanHistoryAdapter
                        binding.progressBar.visibility = View.GONE
                        loanHistoryAdapter.setLoanHistoryList(resource.data)
                    }
                    is MyResource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        MyAlertDialog.show(requireContext(), R.drawable.icon_cancel, "Failed", resource.message.toString())
                    }
                    is MyResource.Empty -> {
                        binding.progressBar.visibility = View.GONE
                        binding.viewEmpty.root.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setRequest() {
        when (activity?.intent?.getStringExtra(PURPOSE)) {
            "member_pending_loan" -> {
                (activity as AppCompatActivity?)?.supportActionBar?.title = "Menunggu Persetujuan"
                viewModel.getAllPendingLoan(sessionManager.fetchAuthToken().toString())
            }
            "member_ongoing_loan" -> {
                (activity as AppCompatActivity?)?.supportActionBar?.title = "Sedang Dipinjam"
                viewModel.getAllOngoingLoan(sessionManager.fetchAuthToken().toString())
            }
            "member_rejected_loan" -> {
                (activity as AppCompatActivity?)?.supportActionBar?.title = "Daftar Peminjaman Ditolak"
                viewModel.getAllRejectedLoan(sessionManager.fetchAuthToken().toString())
            }
            "member_overdue_loan" -> {
                (activity as AppCompatActivity?)?.supportActionBar?.title = "Sudah Melewati Batas Waktu"
                viewModel.getAllOverdueLoan(sessionManager.fetchAuthToken().toString())
            }
            "member_finish_loan" -> {
                (activity as AppCompatActivity?)?.supportActionBar?.title = "History Peminjaman"
                viewModel.getAllFinishLoan(sessionManager.fetchAuthToken().toString())
            }
        }
    }

    companion object{
        const val PURPOSE = "menu_extra_data"
    }
}