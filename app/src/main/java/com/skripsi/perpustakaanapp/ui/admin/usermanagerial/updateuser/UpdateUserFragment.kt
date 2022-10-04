package com.skripsi.perpustakaanapp.ui.admin.usermanagerial.updateuser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.models.User
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.Resource
import com.skripsi.perpustakaanapp.databinding.FragmentUpdateUserBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.utils.setSingleClickListener
import okhttp3.MultipartBody

class UpdateUserFragment : BottomSheetDialogFragment() {

    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: UpdateUserViewModel
    private var fragmentUpdateUserBinding: FragmentUpdateUserBinding? = null
    private val binding get() = fragmentUpdateUserBinding

    private var dataUser: User? = null
    private val client = RetrofitClient
    private var gender: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        fragmentUpdateUserBinding = FragmentUpdateUserBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireActivity())
        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
            UpdateUserViewModel::class.java
        )

        binding?.progressBar?.visibility = View.GONE

        getData()
        setGender()

        binding?.buttonSave?.setSingleClickListener {
            askAppointment()
        }
    }


    private fun setGender(): Int? {
        gender = dataUser?.gender
        when(gender) {
            1 -> {
                binding?.rbGenderMale?.isChecked = true
            }
            2 -> {
                binding?.rbGenderFemale?.isChecked = true
            }
        }
        binding?.rbGender?.setOnCheckedChangeListener { _, i ->
            gender =
                when (i) {
                    binding?.rbGenderMale?.id -> 1
                    binding?.rbGenderFemale?.id -> 2
                    else -> -1
                }
        }
        return gender
    }

    private fun getData() {
        dataUser = activity?.intent?.getParcelableExtra(EXTRA_DATA)
        setEditText()
    }

    private fun setEditText() {
        binding?.edtUsername?.setText(dataUser?.username)
        binding?.edtFirstName?.setText(dataUser?.firstName)
        binding?.edtLastName?.setText(dataUser?.lastName)
        binding?.edtEmail?.setText(dataUser?.email)
        binding?.edtPhoneNo?.setText(dataUser?.phoneNo)
        binding?.edtAddress?.setText(dataUser?.address)
    }

    private fun askAppointment() {
        val username = binding?.edtUsername?.text.toString()

        if (username.isEmpty()) {
            binding?.edtUsername?.error = "NISN Tidak Boleh Kosong"
            binding?.edtUsername?.requestFocus()
        } else {
            postUpdateData()
        }
    }

    private fun postUpdateData() {
        viewModel.updateUser(
            sessionManager.fetchAuthToken().toString(),
            binding?.edtUsername?.text.toString(),
            binding?.edtFirstName?.text.toString(),
            binding?.edtLastName?.text.toString(),
            binding?.edtEmail?.text.toString(),
            binding?.edtPhoneNo?.text.toString(),
            binding?.edtAddress?.text.toString(),
            gender!!
        )

        viewModel.resourceUpdateUser.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
                when(resource) {
                    is Resource.Loading -> {
                        binding?.progressBar?.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding?.progressBar?.visibility = View.GONE
                        MyAlertDialog.showAlertDialogEvent(context,
                            R.drawable.icon_checked,
                            resource.data.toString().uppercase(),
                            "Data Berhasil DiUpdate") { _, _ ->
//                            finish()
                        }
                    }
                    is Resource.Error -> {
                        binding?.progressBar?.visibility = View.GONE
                        MyAlertDialog.showAlertDialog(context,
                            R.drawable.icon_cancel,
                            "FAILED",
                            resource.message.toString())
                    }
                }
            }
        }
    }

    companion object{
        const val EXTRA_DATA = "extra_data"
        const val REQUEST_CODE_IMAGE = "request_code_image"
    }


}