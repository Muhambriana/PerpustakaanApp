package com.skripsi.perpustakaanapp.core.adapter

import com.skripsi.perpustakaanapp.R
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.PagerAdapter
import com.skripsi.perpustakaanapp.core.models.CardMenu
import com.skripsi.perpustakaanapp.databinding.ItemCardMenuBinding
import com.skripsi.perpustakaanapp.ui.book.detailbook.DetailBookActivity
import java.util.*


class CardMenuAdapter(private val models: List<CardMenu>, private val context: Context): PagerAdapter(){

    private lateinit var view: View
    private lateinit var  binding: ItemCardMenuBinding

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private var indexPosition: Int = 0
    private lateinit var backgroundColor: MutableList<Drawable>

    override fun getCount(): Int {
        return models.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        view = layoutInflater.inflate(R.layout.item_card_menu, container, false)
        binding = ItemCardMenuBinding.bind(view)

        indexPosition = position
        setBackground()
        setCardData()

        view.setOnClickListener {
            val intent = Intent(context, models[position].destination)
            context.startActivity(intent)
        }

        container.addView(view, 0)
        return view

    }

    private fun setBackground() {
        models[indexPosition].backgroundColor?.let { binding.menu.background = it }
    }

//    private fun setColorList() {
//        backgroundColor = ContextCompat.getDrawable(context, R.drawable.home_gradient_1)?.let { it1 ->
//            ContextCompat.getDrawable(context, R.drawable.home_gradient_2)?.let { it2 ->
//                ContextCompat.getDrawable(context, R.drawable.home_gradient_3)?.let { it3 ->
//                    ContextCompat.getDrawable(context, R.drawable.home_gradient_4)?.let { it4 ->
//                        ContextCompat.getDrawable(context, R.drawable.home_gradient_5)?.let { it5 ->
//                            ContextCompat.getDrawable(context, R.drawable.home_gradient_6)?.let { it6 ->
//                                arrayListOf(it1, it2, it3, it4, it5, it6)
//                            }
//                        }
//                    }
//                }
//            }
//        }!!
//    }

    private fun setCardData() {
        models[indexPosition].image?.let { binding.image.setImageResource(it) }
        binding.title.text = models[indexPosition].title
//        binding.desc.text = models[indexPosition].desc
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}


//
//
//
//class Adapter(models: List<Model>, context: Context) :
//    PagerAdapter() {
//    private val models: List<Model>
//    private var layoutInflater: LayoutInflater? = null
//    private val context: Context
//    override fun getCount(): Int {
//        return models.size
//    }
//
//    override fun isViewFromObject(view: View, `object`: Any): Boolean {
//        return view == `object`
//    }
//
//    override fun instantiateItem(container: ViewGroup, position: Int): Any {
//        layoutInflater = LayoutInflater.from(context)
//        val view: View = layoutInflater.inflate(R.layout.item_ca, container, false)
//        val imageView: ImageView
//        val title: TextView
//        val desc: TextView
//        imageView = view.findViewById(R.id.image)
//        title = view.findViewById(R.id.title)
//        desc = view.findViewById(R.id.desc)
//        imageView.setImageResource(models[position].getImage())
//        title.setText(models[position].getTitle())
//        desc.setText(models[position].getDesc())
//        view.setOnClickListener {
//            val intent = Intent(context, DetailActivity::class.java)
//            intent.putExtra("param", models[position].getTitle())
//            context.startActivity(intent)
//            // finish();
//        }
//        container.addView(view, 0)
//        return view
//    }
//
//    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//        container.removeView(`object` as View)
//    }
//
//    init {
//        this.models = models
//        this.context = context
//    }
//}



