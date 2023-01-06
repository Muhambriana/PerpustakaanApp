package com.skripsi.perpustakaanapp.core.models

import android.graphics.drawable.Drawable

data class CardMenu(

    val image: Int? = null,
    val title: String? = null,
    val desc: String? = null,
    val destination: Class<*>? = null,
    val backgroundColor: Drawable? = null,
    val string_extra: String? = null

)
