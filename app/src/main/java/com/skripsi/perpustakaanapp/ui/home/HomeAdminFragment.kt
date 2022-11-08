package com.skripsi.perpustakaanapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.adapter.CardMenuAdapter
import com.skripsi.perpustakaanapp.core.models.CardMenu
import com.skripsi.perpustakaanapp.databinding.FragmentHomeAdminBinding
import com.skripsi.perpustakaanapp.ui.admin.bookmanagerial.createbook.CreateBookActivity
import com.skripsi.perpustakaanapp.ui.admin.listuser.UserActivity
import com.skripsi.perpustakaanapp.ui.admin.pendingloan.PendingLoanActivity
import com.skripsi.perpustakaanapp.ui.admin.usermanagerial.createnewadmin.CreateNewAdminActivity
import com.skripsi.perpustakaanapp.ui.admin.scanner.ScannerActivity
import com.skripsi.perpustakaanapp.ui.book.listbook.BookActivity
import com.skripsi.perpustakaanapp.ui.loan.LoanActivity
import com.skripsi.perpustakaanapp.ui.listattendance.AttendanceActivity


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
                R.drawable.icon_pending_loan, "Menunggu Persetujuan", null, PendingLoanActivity::class.java, ContextCompat.getDrawable(requireContext(), R.drawable.home_gradient_1)))
        models?.add(
            CardMenu(
                R.drawable.icon_loan, "Sedang Dipinjam", null, LoanActivity::class.java, ContextCompat.getDrawable(requireContext(), R.drawable.home_gradient_2), "admin_ongoing_loan"))
        models?.add(
            CardMenu(
                R.drawable.icon_history_2, "Riwayat Peminjaman", null, LoanActivity::class.java, ContextCompat.getDrawable(requireContext(), R.drawable.home_gradient_6), "admin_finish_loan"))
        models?.add(
            CardMenu(
                R.drawable.icon_attendant_list, "Daftar Absen", null, AttendanceActivity::class.java, ContextCompat.getDrawable(requireContext(), R.drawable.home_gradient_3)))
        models?.add(
            CardMenu(
                R.drawable.icon_scanner, "Scan Pengunjung", null, ScannerActivity::class.java, ContextCompat.getDrawable(requireContext(), R.drawable.home_gradient_4), "attendance"))
        models?.add(
            CardMenu(
                R.drawable.icon_scanner, "Scan Pengembalian Buku", null, ScannerActivity::class.java, ContextCompat.getDrawable(requireContext(), R.drawable.home_gradient_5), "returning_book"))
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