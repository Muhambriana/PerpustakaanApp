package com.skripsi.perpustakaanapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.google.android.material.snackbar.Snackbar
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.adapter.CardMenuAdapter
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.models.CardMenu
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
import com.skripsi.perpustakaanapp.ui.member.listattendance.AttendanceActivity
import com.skripsi.perpustakaanapp.ui.userprofile.UserProfileActivity
import com.skripsi.perpustakaanapp.utils.NetworkInfo


class HomeAdminFragment : Fragment() {

    private var fragmentHomeAdminBinding: FragmentHomeAdminBinding? = null
    private val binding get() = fragmentHomeAdminBinding

    private var models: MutableList<CardMenu>? = null
    private var adapter: CardMenuAdapter? = null
    private var viewPager: ViewPager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        fragmentHomeAdminBinding = FragmentHomeAdminBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setDefaultMenuListener()
        setModels()
        setAdapter()
        setMenu()
    }

    private fun setDefaultMenuListener() {
        val clickListener = View.OnClickListener { view ->
            when (view.id){
                binding?.cardMenuCreateBook?.id -> {
                    val intent = Intent(activity, CreateBookActivity::class.java)
                    startActivity(intent)
                }
                binding?.cardMenuCreateAdmin?.id -> {
                    val intent = Intent(activity, CreateNewAdminActivity::class.java)
                    startActivity(intent)
                }
                binding?.cardMenuListBook?.id -> {
                    val intent = Intent(activity, BookActivity::class.java)
                    startActivity(intent)
                }
                binding?.cardMenuListUser?.id -> {
                    val intent = Intent(activity, UserActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        binding?.cardMenuCreateBook?.setOnClickListener(clickListener)
        binding?.cardMenuCreateAdmin?.setOnClickListener(clickListener)
        binding?.cardMenuListBook?.setOnClickListener(clickListener)
        binding?.cardMenuListUser?.setOnClickListener(clickListener)
    }

    private fun setModels() {
        models = mutableListOf()
        models?.add(
            CardMenu(
                R.drawable.ic_profile, "Menunggu Persetujuan", null, PendingLoanActivity::class.java, ContextCompat.getDrawable(requireContext(), R.drawable.home_gradient_1)))
        models?.add(
            CardMenu(
                R.drawable.ic_course_book, "Sedang Dipinjam", null, UserProfileActivity::class.java, ContextCompat.getDrawable(requireContext(), R.drawable.home_gradient_2)))
        models?.add(
            CardMenu(
                R.drawable.ic_course_plan, "Daftar Absen", null, AttendanceActivity::class.java, ContextCompat.getDrawable(requireContext(), R.drawable.home_gradient_3)))
        models?.add(
            CardMenu(
                R.drawable.ic_attendance_recap, "Scan Pengunjung", null, ScannerActivity::class.java, ContextCompat.getDrawable(requireContext(), R.drawable.home_gradient_4)))
        models?.add(
            CardMenu(
                R.drawable.ic_gpa_reult, "Scan Pengembalian Buku", null, ScannerActivity::class.java, ContextCompat.getDrawable(requireContext(), R.drawable.home_gradient_5)))
    }

    private fun setAdapter() {
        adapter = models?.let { CardMenuAdapter(it, requireContext()) }
    }

    private fun setMenu() {
        viewPager = binding?.viewPager

        viewPager?.adapter = adapter

        viewPager?.setPadding(10, 0, 340, 0)
        viewPager?.pageMargin = 20
    }

}