package com.skripsi.perpustakaanapp.core.models

import com.google.gson.annotations.SerializedName

data class BookCreateResponse(
    @field:SerializedName("code")
    val code: Int,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName ("bookItems")
    val bookItems: List<Book>
)
