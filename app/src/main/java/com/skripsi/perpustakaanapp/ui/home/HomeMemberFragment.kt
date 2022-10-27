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
import com.skripsi.perpustakaanapp.ui.book.ebook.EbookActivity
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
        sessionManager = SessionManager(requireContext())

        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
            HomeViewModel::class.java
        )
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
                binding?.cardPdf?.id -> {
                    val intent = Intent(activity, EbookActivity::class.java)
                    startActivity(intent)
                }
            }

        }

        binding?.cardBookList?.setOnClickListener(clickListener)
        binding?.cardLoanHistoryMember?.setOnClickListener(clickListener)
        binding?.cardUserProfile?.setOnClickListener(clickListener)
        binding?.cardAttendanceList?.setOnClickListener(clickListener)
        binding?.cardAttendance?.setOnClickListener(clickListener)
        binding?.cardPdf?.setOnClickListener(clickListener)
    }

}