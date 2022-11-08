package com.skripsi.perpustakaanapp.ui.admin.usermanagerial.updateuser

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
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
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.databinding.FragmentUpdateUserBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.ui.MySnackBar
import com.skripsi.perpustakaanapp.ui.userprofile.UserProfileActivity
import com.skripsi.perpustakaanapp.utils.setSingleClickListener

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

        binding?.edtFirstName?.filters?.plus(InputFilter.AllCaps())
        binding?.edtLastName?.filters?.plus(InputFilter.AllCaps())
        binding?.progressBar?.visibility = View.GONE

        getData()
        setGender()

        buttonListener()
    }

    private fun getData() {
        val bundle: Bundle? = this.arguments
        dataUser = bundle?.getParcelable(FRAGMENT_EXTRA_DATA)
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

    private fun buttonListener() {
        binding?.buttonBack?.setSingleClickListener {
            dismiss()
        }
        binding?.buttonSave?.setSingleClickListener {
            askAppointment()
        }
    }

    private fun askAppointment() {
        val username = binding?.edtUsername?.text.toString()

        if (username.isEmpty()) {
            binding?.edtUsername?.error = "NISN Tidak Boleh Kosong"
            binding?.edtUsername?.requestFocus()
        } else {
            MyAlertDialog.showWith2Event(
                requireContext(),
                null,
                resources.getString(R.string.data_confirmation),
                resources.getString(R.string.confirmation_yes),
                resources.getString(R.string.confirmation_recheck),
                {_,_ ->
                    postUpdateData()
                }, {_,_ ->

                })
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
                    is MyResource.Loading -> {
                        binding?.progressBar?.visibility = View.VISIBLE
                    }
                    is MyResource.Success -> {
                        binding?.progressBar?.visibility = View.GONE
                        activity?.setResult(RESULT_OK)
                        MySnackBar.showBlack(binding?.root, resource.data.toString())

                        val intent = Intent(context, UserProfileActivity::class.java)
                        intent.putExtra(UserProfileActivity.USERNAME, dataUser?.username)
                        startActivity(intent)

                        activity?.finish()
                    }
                    is MyResource.Error -> {
                        binding?.progressBar?.visibility = View.GONE
                        MySnackBar.showRed(binding?.root, resource.message.toString())
                    }
                }
            }
        }
    }

    companion object{
        const val FRAGMENT_EXTRA_DATA = "extra_data"
        const val REQUEST_CODE_IMAGE = "request_code_image"
    }


}