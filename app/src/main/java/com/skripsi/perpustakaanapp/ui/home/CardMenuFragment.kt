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
    private var viewPager: ViewPager? = null
    private var viewPager2: ViewPager? = null
    private var adapter: CardMenuAdapter? = null

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
        models = null
        adapter = null
        setModels2()
        setAdapter()
        setMenu2()

        //set model
        //set adapter
        //set menu1
        //model null
        //adapter null
        //set model
        //set adapter
        //set menu2
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

        viewPager?.setPadding(180, 0, 180, 0)
        viewPager?.pageMargin = 20

        viewPager?.setOnPageChangeListener(object : OnPageChangeListener{
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun setModels2() {
        models = mutableListOf()
        models?.add(
            CardMenu(
                R.drawable.icon_warning,
                "Item 1",
                "Brochure is an informative paper document (often also used for advertising) that can be folded into a template",
                BookActivity::class.java,
                ContextCompat.getDrawable(requireContext(), R.drawable.home_gradient_1)))
        models?.add(CardMenu(R.drawable.icon_no_connection, "Item 2", "Kocak", UserProfileActivity::class.java))
        models?.add(CardMenu(R.drawable.icon_logout, "Item 3", "R.drawable.icon_checked, \"Item 2\", \"Kocak\"", PendingLoanActivity::class.java))
    }

    private fun setMenu2() {
        setAdapter()

        viewPager2 = binding?.viewPager2

        viewPager2?.adapter = adapter

        viewPager2?.setPadding(180, 0, 180, 0)
        viewPager2?.pageMargin = 20
    }

}


//class MainActivity : AppCompatActivity() {
//    var viewPager: ViewPager? = null
//    var adapter: Adapter? = null
//    var models: MutableList<Model>? = null
//    var colors: Array<Int>? = null
//    var argbEvaluator: ArgbEvaluator = ArgbEvaluator()
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        models = ArrayList()
//        models!!.add(Model(R.drawable.brochure,
//            "Brochure",
//            "Brochure is an informative paper document (often also used for advertising) that can be folded into a template"))
//        models!!.add(Model(R.drawable.sticker,
//            "Sticker",
//            "Sticker is a type of label: a piece of printed paper, plastic, vinyl, or other material with pressure sensitive adhesive on one side"))
//        models!!.add(Model(R.drawable.poster,
//            "Poster",
//            "Poster is any piece of printed paper designed to be attached to a wall or vertical surface."))
//        models!!.add(Model(R.drawable.namecard,
//            "Namecard",
//            "Business cards are cards bearing business information about a company or individual."))
//        adapter = Adapter(models, this)
//        viewPager = findViewById(R.id.viewPager)
//        viewPager.setAdapter(adapter)
//        viewPager.setPadding(130, 0, 130, 0)
//        val colors_temp = arrayOf(
//            resources.getColor(R.color.color1),
//            resources.getColor(R.color.color2),
//            resources.getColor(R.color.color3),
//            resources.getColor(R.color.color4)
//        )
//        colors = colors_temp
//        viewPager.setOnPageChangeListener(object : OnPageChangeListener {
//            override fun onPageScrolled(
//                position: Int,
//                positionOffset: Float,
//                positionOffsetPixels: Int,
//            ) {
//                if (position < adapter.getCount() - 1 && position < colors!!.size - 1) {
//                    viewPager?.setBackgroundColor(
//                        (argbEvaluator.evaluate(
//                            positionOffset,
//                            colors!![position],
//                            colors!![position + 1]
//                        ) as Int)
//                    )
//                } else {
//                    viewPager.setBackgroundColor(colors!![colors!!.size - 1])
//                }
//            }
//
//            override fun onPageSelected(position: Int) {}
//            override fun onPageScrollStateChanged(state: Int) {}
//        })
//    }
//}


//===========================================================================================================
//package com.skripsi.perpustakaanapp.ui.home
//
//import android.animation.ArgbEvaluator
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Adapter
//import androidx.appcompat.app.AppCompatActivity
//import androidx.fragment.app.Fragment
//import androidx.viewpager.widget.ViewPager
//import androidx.viewpager.widget.ViewPager.OnPageChangeListener
//import com.skripsi.perpustakaanapp.R
//import com.skripsi.perpustakaanapp.core.adapter.CardMenuAdapter
//import com.skripsi.perpustakaanapp.core.models.CardMenu
//import com.skripsi.perpustakaanapp.databinding.FragmentCardMenuBinding
//
//
//class CardMenuFragment : Fragment() {
//
//    private var fragmentCardMenuBinding: FragmentCardMenuBinding? = null
//    private val binding get() = fragmentCardMenuBinding
//
//    private var models: MutableList<CardMenu>? = mutableListOf(CardMenu(R.drawable.icon_checked,
//        "Brochure",
//        "Brochure is an informative paper document (often also used for advertising) that can be folded into a template"))
//    private var viewPager: ViewPager? = null
//    private var adapter: CardMenuAdapter? = null
//    private val argbEvaluator: ArgbEvaluator = ArgbEvaluator()
//    private lateinit var colors: Array<Int>
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?,
//    ): View? {
//        fragmentCardMenuBinding = FragmentCardMenuBinding.inflate(layoutInflater, container, false)
//        return binding?.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        setModels()
//        setCard()
//    }
//
//    private fun setModels() {
//        models?.add(CardMenu(R.drawable.icon_checked,
//            "Brochure",
//            "Brochure is an informative paper document (often also used for advertising) that can be folded into a template"))
//        models?.add(CardMenu(R.drawable.icon_cancel,
//            "Sticker",
//            "Sticker is a type of label: a piece of printed paper, plastic, vinyl, or other material with pressure sensitive adhesive on one side"))
//
//        if(models!=null) {
//            adapter = CardMenuAdapter(models!!, requireContext())
//        } else {
//            println("kocak kosong $models")
//        }
//    }
//
//    private fun setCard() {
//        viewPager = binding?.viewPager
//        viewPager?.adapter = adapter
//        viewPager?.setPadding(130, 0, 130, 0)
//        val colors_temp = arrayOf(
//            resources.getColor(R.color.color1),
//            resources.getColor(R.color.color2),
//            resources.getColor(R.color.color3),
//            resources.getColor(R.color.color4)
//        )
//        colors = colors_temp
//        viewPager?.setOnPageChangeListener(object : OnPageChangeListener {
//            override fun onPageScrolled(
//                position: Int,
//                positionOffset: Float,
//                positionOffsetPixels: Int,
//            ) {
//                if (position < adapter!!.count - 1 && position < colors!!.size - 1) {
//                    viewPager?.setBackgroundColor(
//                        (argbEvaluator?.evaluate(
//                            positionOffset,
//                            colors[position],
//                            colors[position + 1]
//                        ) as Int)
//                    )
//                } else {
//                    viewPager?.setBackgroundColor(colors[colors.size - 1])
//                }
//            }
//
//            override fun onPageSelected(position: Int) {}
//            override fun onPageScrollStateChanged(state: Int) {}
//        })
//    }
//
//}
//
//
////class MainActivity : AppCompatActivity() {
////    var viewPager: ViewPager? = null
////    var adapter: Adapter? = null
////    var models: MutableList<Model>? = null
////    var colors: Array<Int>? = null
////    var argbEvaluator: ArgbEvaluator = ArgbEvaluator()
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////        setContentView(R.layout.activity_main)
////        models = ArrayList()
////        models!!.add(Model(R.drawable.brochure,
////            "Brochure",
////            "Brochure is an informative paper document (often also used for advertising) that can be folded into a template"))
////        models!!.add(Model(R.drawable.sticker,
////            "Sticker",
////            "Sticker is a type of label: a piece of printed paper, plastic, vinyl, or other material with pressure sensitive adhesive on one side"))
////        models!!.add(Model(R.drawable.poster,
////            "Poster",
////            "Poster is any piece of printed paper designed to be attached to a wall or vertical surface."))
////        models!!.add(Model(R.drawable.namecard,
////            "Namecard",
////            "Business cards are cards bearing business information about a company or individual."))
////        adapter = Adapter(models, this)
////        viewPager = findViewById(R.id.viewPager)
////        viewPager.setAdapter(adapter)
////        viewPager.setPadding(130, 0, 130, 0)
////        val colors_temp = arrayOf(
////            resources.getColor(R.color.color1),
////            resources.getColor(R.color.color2),
////            resources.getColor(R.color.color3),
////            resources.getColor(R.color.color4)
////        )
////        colors = colors_temp
////        viewPager.setOnPageChangeListener(object : OnPageChangeListener {
////            override fun onPageScrolled(
////                position: Int,
////                positionOffset: Float,
////                positionOffsetPixels: Int,
////            ) {
////                if (position < adapter.getCount() - 1 && position < colors!!.size - 1) {
////                    viewPager?.setBackgroundColor(
////                        (argbEvaluator.evaluate(
////                            positionOffset,
////                            colors!![position],
////                            colors!![position + 1]
////                        ) as Int)
////                    )
////                } else {
////                    viewPager.setBackgroundColor(colors!![colors!!.size - 1])
////                }
////            }
////
////            override fun onPageSelected(position: Int) {}
////            override fun onPageScrollStateChanged(state: Int) {}
////        })
////    }
////}