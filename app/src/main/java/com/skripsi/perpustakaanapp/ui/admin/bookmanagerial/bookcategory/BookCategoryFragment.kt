package com.skripsi.perpustakaanapp.ui.admin.bookmanagerial.bookcategory

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.InputFilter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.databinding.FragmentBookCategoryBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.ui.MySnackBar

class BookCategoryFragment : DialogFragment() {

    private lateinit var sessionManager: SessionManager
    private lateinit var binding: FragmentBookCategoryBinding
    private lateinit var viewModel: BookCategoryViewModel

    private val client = RetrofitClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View {
        binding = FragmentBookCategoryBinding.inflate(LayoutInflater.from(inflater.context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sessionManager = SessionManager(requireActivity())

        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
            BookCategoryViewModel::class.java
        )

        buttonListener()
    }

    private fun buttonListener() {
        binding.upload.setOnClickListener {
            MyAlertDialog.showWith2Event(
                requireContext(),
                null,
                resources.getString(R.string.data_confirmation),
                resources.getString(R.string.confirmation_yes),
                resources.getString(R.string.confirmation_recheck),
                {_,_ ->
                    postNewCategory()
                }, {_,_ ->

                })

        }
    }

    private fun postNewCategory() {

        viewModel.createCategory(sessionManager.fetchAuthToken().toString(), binding.edtCategoryName.text.toString())

        viewModel.resourceCreateCategory.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
                when (resource) {
                    is MyResource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is MyResource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.edtCategoryName.text.clear()
                        MySnackBar.showBlack(binding.root, resource.data.toString())
                    }
                    is MyResource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        MySnackBar.showRed(binding.root, resource.message.toString())
                    }
                    else -> {}
                }
            }
        }
    }

}