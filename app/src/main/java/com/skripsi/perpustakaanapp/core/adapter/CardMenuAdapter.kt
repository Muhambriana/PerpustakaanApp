package com.skripsi.perpustakaanapp.core.adapter

import android.R
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

    override fun getCount(): Int {
        return models.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = LayoutInflater.from(context)
        val view: View = layoutInflater.inflate(com.skripsi.perpustakaanapp.R.layout.item_card_menu, container, false)
        val binding = ItemCardMenuBinding.bind(view)

        binding.menu.background = ContextCompat.getDrawable(context, com.skripsi.perpustakaanapp.R.drawable.home_gradient_1);

        models[position].image?.let { binding.image.setImageResource(it) }
        binding.title.text = models[position].title
        binding.desc.text = models[position].desc

        view.setOnClickListener {
            val intent = Intent(context, DetailBookActivity::class.java)
            context.startActivity(intent)
        }

        container.addView(view, 0)
        return view

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



