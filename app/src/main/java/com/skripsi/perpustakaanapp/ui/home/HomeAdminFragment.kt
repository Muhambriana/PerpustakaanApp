package com.skripsi.perpustakaanapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.google.android.material.snackbar.Snackbar
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.databinding.FragmentHomeAdminBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.ui.admin.bookmanagerial.createbook.CreateBookActivity
import com.skripsi.perpustakaanapp.ui.admin.listuser.UserActivity
import com.skripsi.perpustakaanapp.ui.admin.pendingloan.PendingLoanActivity
import com.skripsi.perpustakaanapp.ui.admin.usermanagerial.createnewadmin.CreateNewAdminActivity
import com.skripsi.perpustakaanapp.ui.admin.usermanagerial.scanattendance.ScannerActivity
import com.skripsi.perpustakaanapp.ui.book.listbook.BookActivity
import com.skripsi.perpustakaanapp.ui.login.LoginActivity
import com.skripsi.perpustakaanapp.ui.userprofile.UserProfileActivity
import com.skripsi.perpustakaanapp.utils.NetworkInfo


class HomeAdminFragment : Fragment() {

    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: HomeViewModel

    private var fragmentHomeAdminBinding: FragmentHomeAdminBinding? = null
    private val binding get() = fragmentHomeAdminBinding
    private val client = RetrofitClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        fragmentHomeAdminBinding = FragmentHomeAdminBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        firstInitialization()
        firstInitialization()
        cardListener()
    }

    private fun firstInitialization() {
//        activity?.actionBar?.hide()
//        (activity as AppCompatActivity?)?.setSupportActionBar(binding?.myToolbar)

        sessionManager = SessionManager(requireContext())

        if (activity?.intent?.extras!=null){
//            binding?.toolbarTitle?.text = "Hi, ${activity?.intent?.getStringExtra("FIRST_NAME")}"
//            userProfile()
        }

        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
            HomeViewModel::class.java
        )
    }

    private fun cardListener() {
        val clickListener = View.OnClickListener { view ->
            when (view.id){
                binding?.cardCreateBook?.id -> {
                    val intent = Intent(activity, CreateBookActivity::class.java)
                    startActivity(intent)
                }
                binding?.cardCreateAdmin?.id -> {
                    val intent = Intent(activity, CreateNewAdminActivity::class.java)
                    startActivity(intent)
                }
                binding?.cardBookList?.id -> {
                    val intent = Intent(activity, BookActivity::class.java)
                    startActivity(intent)
                }
                binding?.cardPendingLoanList?.id -> {
                    val intent = Intent(activity, PendingLoanActivity::class.java)
                    startActivity(intent)
                }
                binding?.cardUserList?.id -> {
                    val intent = Intent(activity, UserActivity::class.java)
                    startActivity(intent)
                }
                binding?.cardScanVisitors?.id -> {
                    val intent = Intent(activity, ScannerActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        binding?.cardCreateBook?.setOnClickListener(clickListener)
        binding?.cardCreateAdmin?.setOnClickListener(clickListener)
        binding?.cardBookList?.setOnClickListener(clickListener)
        binding?.cardPendingLoanList?.setOnClickListener(clickListener)
        binding?.cardUserList?.setOnClickListener(clickListener)
        binding?.cardScanVisitors?.setOnClickListener(clickListener)
    }

    private fun userProfile() {
//        binding?.toolbarIcon?.let {
//            Glide.with(this)
//                .load(NetworkInfo.AVATAR_IMAGE_BASE_URL+activity?.intent?.getStringExtra("AVATAR"))
//                .signature(ObjectKey(System.currentTimeMillis().toString()))
//                .centerCrop()
//                .into(it)
//        }
//        binding?.toolbarIcon?.setOnClickListener {
//            val intent = Intent(activity, UserProfileActivity::class.java)
//            intent.putExtra(UserProfileActivity.USERNAME, sessionManager.fetchUsername())
//            startActivity(intent)
//        }
    }
}