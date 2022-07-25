package com.skripsi.perpustakaanapp.core.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book(

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("edition")
    val edition: String? = null,

    @field:SerializedName("author")
    val author: String? = null,

    @field:SerializedName("publisher")
    val publisher: String? = null,

    @field:SerializedName("publisherDate")
    val publisherDate: String? = null,

    @field:SerializedName("copies")
    val copies: String? = null,

    @field:SerializedName("source")
    val source: String? = null,

    @field:SerializedName("remark")
    val remark: String? = null,

    @field:SerializedName("price")
    val price: Double? = null,

) : Parcelable
