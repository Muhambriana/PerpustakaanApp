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
import com.skripsi.perpustakaanapp.databinding.FragmentHomeMemberBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.ui.SettingsActivity
import com.skripsi.perpustakaanapp.ui.book.listbook.BookActivity
import com.skripsi.perpustakaanapp.ui.login.LoginActivity
import com.skripsi.perpustakaanapp.ui.member.listattendance.AttendanceActivity
import com.skripsi.perpustakaanapp.ui.member.loanhistory.LoanHistoryActivity
import com.skripsi.perpustakaanapp.ui.userprofile.UserProfileActivity
import com.skripsi.perpustakaanapp.utils.NetworkInfo
import com.skripsi.perpustakaanapp.utils.WindowTouchableHelper

class HomeMemberFragment : Fragment() {

    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: HomeViewModel

    private var fragmentHomMemberFragment: FragmentHomeMemberBinding? = null
    private val binding get() = fragmentHomMemberFragment
    private val client = RetrofitClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        fragmentHomMemberFragment = FragmentHomeMemberBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        firstInitialization()
        cardListener()
    }


    private fun firstInitialization() {
        activity?.actionBar?.hide()
        (activity as AppCompatActivity?)?.setSupportActionBar(binding?.myToolbar)

        sessionManager = SessionManager(requireContext())

        if (activity?.intent?.extras!=null){
            binding?.toolbarTitle?.text = "Hi, ${activity?.intent?.getStringExtra("FIRST_NAME")}"
            userProfile()
        }

        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
            HomeViewModel::class.java
        )
    }

    private fun userProfile() {
        binding?.toolbarIcon?.let {
            Glide.with(this)
                .load(NetworkInfo.AVATAR_IMAGE_BASE_URL+activity?.intent?.getStringExtra("AVATAR"))
                .signature(ObjectKey(System.currentTimeMillis().toString()))
                .centerCrop()
                .into(it)
        }
        binding?.toolbarIcon?.setOnClickListener {
            val intent = Intent(activity, UserProfileActivity::class.java)
            intent.putExtra(UserProfileActivity.USERNAME, sessionManager.fetchUsername())
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.activity_home_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout_menu -> {
                userLogout()
                true
            }
            else -> true
        }
    }

    private fun cardListener() {
        val clickListener = View.OnClickListener {view ->
            when (view.id){
                binding?.cardBookList?.id -> {
                    val intent = Intent(activity, BookActivity::class.java)
                    startActivity(intent)
                }
                binding?.cardLoanHistoryMember?.id -> {
                    val intent = Intent(activity, LoanHistoryActivity::class.java)
                    startActivity(intent)
                }
                binding?.cardUserProfile?.id -> {
                    val intent = Intent(activity, UserProfileActivity::class.java)
                    startActivity(intent)
                }
                binding?.cardAttendanceList?.id -> {
                    val intent = Intent(activity, AttendanceActivity::class.java)
                    startActivity(intent)
                }
                binding?.cardAttendance?.id -> {
                    val intent = Intent(activity, SettingsActivity::class.java)
                    startActivity(intent)
                }
            }

        }

        binding?.cardBookList?.setOnClickListener(clickListener)
        binding?.cardLoanHistoryMember?.setOnClickListener(clickListener)
        binding?.cardUserProfile?.setOnClickListener(clickListener)
        binding?.cardAttendanceList?.setOnClickListener(clickListener)
        binding?.cardAttendance?.setOnClickListener(clickListener)
    }

    private fun userLogout() {
        sessionManager = SessionManager(requireContext())

        viewModel.userLogout(token = sessionManager.fetchAuthToken().toString())

        viewModel.resourceLogout.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
                when (resource) {
                    is MyResource.Loading -> {
                        WindowTouchableHelper.disable(activity)
                        binding?.progressBar?.visibility = View.VISIBLE
                    }
                    is MyResource.Success -> {
                        WindowTouchableHelper.enable(activity)
                        binding?.progressBar?.visibility = View.VISIBLE
                        startIntentBackToLogin()
                    }
                    is MyResource.Error -> {
                        WindowTouchableHelper.enable(activity)
                        binding?.progressBar?.visibility = View.GONE
                        MyAlertDialog.show(context, R.drawable.icon_cancel, "ERROR", resource.message.toString())
                    }
                }
            }
        }
    }

    private fun startIntentBackToLogin() {
        val intent = Intent(activity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        activity?.finish()
    }

}