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
import com.skripsi.perpustakaanapp.ui.pendingloan.PendingLoanActivity
import com.skripsi.perpustakaanapp.ui.book.listbook.BookActivity
import com.skripsi.perpustakaanapp.ui.loan.LoanActivity
import com.skripsi.perpustakaanapp.ui.listattendance.AttendanceActivity

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
                    val intent = Intent(activity, LoanActivity::class.java)
                    intent.putExtra("menu_extra_data", "member_ongoing_loan")
                    startActivity(intent)
                }
                binding?.cardMenuLoanHistory?.id -> {
                    val intent = Intent(activity, LoanActivity::class.java)
                    intent.putExtra("menu_extra_data", "member_finish_loan")
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
                R.drawable.icon_ebook, "Baca e-Book", null, BookActivity::class.java, ContextCompat.getDrawable(requireContext(), R.drawable.home_gradient_1), "ebook"))
        models?.add(
            CardMenu(
                R.drawable.icon_overdue, "Telat Dikembalikan", null, LoanActivity::class.java, ContextCompat.getDrawable(requireContext(), R.drawable.home_gradient_4), "member_overdue_loan"))
        models?.add(
            CardMenu(
                R.drawable.icon_attendant_list, "History Kunjungan", null, AttendanceActivity::class.java, ContextCompat.getDrawable(requireContext(), R.drawable.home_gradient_3)))
        models?.add(
            CardMenu(
                R.drawable.icon_rejected, "Peminjaman Ditolak", null, LoanActivity::class.java, ContextCompat.getDrawable(requireContext(), R.drawable.home_gradient_2), "member_rejected_loan"))
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