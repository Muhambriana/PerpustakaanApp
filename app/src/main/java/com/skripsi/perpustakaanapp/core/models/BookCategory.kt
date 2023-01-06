package com.skripsi.perpustakaanapp.core.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookCategory(

    @field:SerializedName("id")
    val categoryId: String? = null,

    @field:SerializedName("name")
    val categoryName: String? = null,
):Parcelable
