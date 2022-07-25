package com.skripsi.perpustakaanapp.core.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.skripsi.perpustakaanapp.databinding.ItemMenuBinding
import com.skripsi.perpustakaanapp.core.models.MenuData


class MenuAdapter(val listMenuData: ArrayList<MenuData>) : RecyclerView.Adapter<MenuAdapter.ViewHolder>(){
    inner class ViewHolder(private val binding: ItemMenuBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(data: MenuData){
            with(binding){
                Glide.with(itemView)
                    .load(data.Image)
                    .apply(RequestOptions().override(180,180))
                    .into(img)
                tvItemList.text = data.title
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
       ViewHolder(ItemMenuBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(listMenuData[position])

    override fun getItemCount(): Int = listMenuData.size
}