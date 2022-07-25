package com.skripsi.perpustakaanapp.utils

import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.models.MenuData

object MenuResource {
    private val title = arrayOf(
        "A",
        "B",
//        "C",
//        "D"
        )

    private val image = intArrayOf(
        R.drawable.ic_baseline_pan_tool_24,
        R.drawable.ic_baseline_lock_24,
//        R.drawable.c,
//        R.drawable.d,
//        R.drawable.e,
//        R.drawable.f,
//        R.drawable.g,
//        R.drawable.h,
//        R.drawable.i,
//        R.drawable.j,
//        R.drawable.k,
//        R.drawable.l,
//        R.drawable.m,
//        R.drawable.n,
//        R.drawable.o,
//        R.drawable.p,
//        R.drawable.q,
//        R.drawable.r,
//        R.drawable.s,
//        R.drawable.t,
//        R.drawable.u,
//        R.drawable.v,
//        R.drawable.w,
//        R.drawable.x,
//        R.drawable.y,
//        R.drawable.z
    )

    val listResource : ArrayList<MenuData>
            get(){
                val list = arrayListOf<MenuData>()
                for (posistion in title.indices){
                    val menu = MenuData()
                    menu.Image = image[posistion]
                    menu.title = title[posistion]
                    list.add(menu)
                }
                return list
            }
}