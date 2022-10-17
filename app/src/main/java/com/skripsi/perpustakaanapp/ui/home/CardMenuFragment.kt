package com.skripsi.perpustakaanapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.adapter.CardMenuAdapter
import com.skripsi.perpustakaanapp.core.models.CardMenu
import com.skripsi.perpustakaanapp.databinding.FragmentCardMenuBinding
import com.skripsi.perpustakaanapp.ui.admin.pendingloan.PendingLoanActivity
import com.skripsi.perpustakaanapp.ui.book.listbook.BookActivity
import com.skripsi.perpustakaanapp.ui.userprofile.UserProfileActivity


class CardMenuFragment : Fragment() {

    private var fragmentCardMenuBinding: FragmentCardMenuBinding? = null
    private val binding get() = fragmentCardMenuBinding

    private var models: MutableList<CardMenu>? = null
    private var adapter: CardMenuAdapter? = null
    private var viewPager: ViewPager? = null
    private var viewPager2: ViewPager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        fragmentCardMenuBinding = FragmentCardMenuBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setModels1()
        setAdapter()
        setMenu1()
//        models = null
//        adapter = null
//        setModels2()
//        setAdapter()
//        setMenu2()
    }

    private fun setModels1() {
        models = mutableListOf()
        models?.add(
            CardMenu(
                R.drawable.icon_edit_image,
                "Item 1",
                "Brochure is an informative paper document (often also used for advertising) that can be folded into a template",
                BookActivity::class.java,
                ContextCompat.getDrawable(requireContext(), R.drawable.home_gradient_1)))
        models?.add(CardMenu(R.drawable.icon_checked, "Item 2", "Kocak", UserProfileActivity::class.java))
        models?.add(CardMenu(R.drawable.icon_cancel, "Item 3", "R.drawable.icon_checked, \"Item 2\", \"Kocak\"", PendingLoanActivity::class.java))
    }

    private fun setAdapter() {
        adapter = models?.let { CardMenuAdapter(it, requireContext()) }
    }

    private fun setMenu1() {
       setAdapter()

        viewPager = binding?.viewPager

        viewPager?.adapter = adapter

        viewPager?.setPadding(10, 0, 340, 0)
        viewPager?.pageMargin = 20

        viewPager?.setOnPageChangeListener(object : OnPageChangeListener{
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

//    private fun setModels2() {
//        models = mutableListOf()
//        models?.add(
//            CardMenu(
//                R.drawable.icon_warning,
//                "Item 1",
//                "Brochure is an informative paper document (often also used for advertising) that can be folded into a template",
//                BookActivity::class.java,
//                ContextCompat.getDrawable(requireContext(), R.drawable.home_gradient_1)))
//        models?.add(CardMenu(R.drawable.icon_no_connection, "Item 2", "Kocak", UserProfileActivity::class.java))
//        models?.add(CardMenu(R.drawable.icon_logout, "Item 3", "R.drawable.icon_checked, \"Item 2\", \"Kocak\"", PendingLoanActivity::class.java))
//    }
//
//    private fun setMenu2() {
//        setAdapter()
//
//        viewPager2 = binding?.viewPager2
//
//        viewPager2?.adapter = adapter
//
//        viewPager2?.setPadding(180, 0, 180, 0)
//        viewPager2?.pageMargin = 20
//    }

}