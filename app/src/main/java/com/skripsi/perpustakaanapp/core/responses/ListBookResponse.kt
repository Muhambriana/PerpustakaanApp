package com.skripsi.perpustakaanapp.core.responses

import com.google.gson.annotations.SerializedName
import com.skripsi.perpustakaanapp.core.models.Book
import com.skripsi.perpustakaanapp.core.models.FavoriteBook

data class ListBookResponse(

    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("bookItems")
    val bookItems: List<Book>? = null
)

data class ListFavoriteResponse(

    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("favoriteItems")
    val favoriteItems: List<Book>? = null
)
