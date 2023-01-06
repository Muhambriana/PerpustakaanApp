package com.skripsi.perpustakaanapp.core.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.models.CardMenu
import com.skripsi.perpustakaanapp.databinding.ItemCardMenuBinding


class CardMenuAdapter(private val models: List<CardMenu>, private val context: Context): PagerAdapter(){

    private lateinit var view: View
    private lateinit var  binding: ItemCardMenuBinding

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private var indexPosition: Int = 0

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
            intent.putExtra("menu_extra_data", models[position].string_extra)
            context.startActivity(intent)
        }

        container.addView(view, 0)
        return view

    }

    private fun setBackground() {
        models[indexPosition].backgroundColor?.let { binding.menu.background = it }
    }

    private fun setCardData() {
        models[indexPosition].image?.let { binding.image.setImageResource(it) }
        binding.title.text = models[indexPosition].title
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}


