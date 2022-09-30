package com.skripsi.perpustakaanapp.ui.admin.usermanagerial.updateuser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.models.User
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.databinding.FragmentUpdateUserBinding
import okhttp3.MultipartBody

class UpdateUserFragment : BottomSheetDialogFragment() {

    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: UpdateUserViewModel
    private var fragmentUpdateUserBinding: FragmentUpdateUserBinding? = null
    private val binding get() = fragmentUpdateUserBinding

    private var dataUser: User? = null
    private val client = RetrofitClient
    private var imageMultipartBody: MultipartBody.Part? = null

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


    }


}