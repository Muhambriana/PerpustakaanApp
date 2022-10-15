package com.skripsi.perpustakaanapp.core.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CardMenu(

    val image: Int? = null,
    val title: String? = null,
    val desc: String? = null

):Parcelable
