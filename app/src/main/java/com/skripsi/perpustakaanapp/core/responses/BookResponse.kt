package com.skripsi.perpustakaanapp.core.responses

import com.google.gson.annotations.SerializedName
import com.skripsi.perpustakaanapp.core.models.Book

data class BookResponse(

    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("bookItems")
    val bookItems: List<Book>
)
