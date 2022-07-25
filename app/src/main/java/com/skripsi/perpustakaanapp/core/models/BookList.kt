package com.skripsi.perpustakaanapp.core.models

import com.google.gson.annotations.SerializedName

data class BookList(

    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("bookItems")
    val bookItems: List<Book>
)
