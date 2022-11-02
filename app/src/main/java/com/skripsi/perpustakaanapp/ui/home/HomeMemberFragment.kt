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
import com.skripsi.perpustakaanapp.databinding.FragmentHomeMemberBinding
import com.skripsi.perpustakaanapp.ui.admin.pendingloan.PendingLoanActivity
import com.skripsi.perpustakaanapp.ui.book.listbook.BookActivity
import com.skripsi.perpustakaanapp.ui.member.listattendance.AttendanceActivity
import com.skripsi.perpustakaanapp.ui.member.loanhistory.LoanHistoryActivity
import com.skripsi.perpustakaanapp.ui.userprofile.UserProfileActivity

class HomeMemberFragment : Fragment() {

    private var fragmentHomMemberBinding: FragmentHomeMemberBinding? = null
    private val binding get() = fragmentHomMemberBinding

    private var models: MutableList<CardMenu>? = null
    private var adapter: CardMenuAdapter? = null
    private var viewPager: ViewPager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        fragmentHomMemberBinding = FragmentHomeMemberBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setDefaultMenuListener()
        setModels()
        setAdapter()
        setMenu()
    }

    private fun setDefaultMenuListener() {
        val clickListener = View.OnClickListener {view ->
            when (view.id){
                binding?.cardMenuListBook?.id -> {
                    val intent = Intent(activity, BookActivity::class.java)
                    startActivity(intent)
                }
                binding?.cardMenuPendingLoan?.id -> {
                    val intent = Intent(activity, PendingLoanActivity::class.java)
                    startActivity(intent)
                }
                binding?.cardMenuOnLoan?.id -> {
                    val intent = Intent(activity, LoanHistoryActivity::class.java)
                    startActivity(intent)
                }
                binding?.cardMenuLoanHistory?.id -> {
                    val intent = Intent(activity, LoanHistoryActivity::class.java)
                    startActivity(intent)
                }
            }

        }

        binding?.cardMenuListBook?.setOnClickListener(clickListener)
        binding?.cardMenuPendingLoan?.setOnClickListener(clickListener)
        binding?.cardMenuOnLoan?.setOnClickListener(clickListener)
        binding?.cardMenuLoanHistory?.setOnClickListener(clickListener)
    }

    private fun setModels() {
        models = mutableListOf()
        models?.add(
            CardMenu(
                R.drawable.icon_ebook, "Baca e-Book", null, AttendanceActivity::class.java, ContextCompat.getDrawable(requireContext(), R.drawable.home_gradient_1)))
        models?.add(
            CardMenu(
                R.drawable.icon_overdue, "Telat Dikembalikan", null, UserProfileActivity::class.java, ContextCompat.getDrawable(requireContext(), R.drawable.home_gradient_4)))
        models?.add(
            CardMenu(
                R.drawable.icon_attendant_list, "History Kunjungan", null, AttendanceActivity::class.java, ContextCompat.getDrawable(requireContext(), R.drawable.home_gradient_3)))
        models?.add(
            CardMenu(
                R.drawable.icon_rejected, "Peminjaman Ditolak", null, AttendanceActivity::class.java, ContextCompat.getDrawable(requireContext(), R.drawable.home_gradient_2)))
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