package com.skripsi.perpustakaanapp.core.models

import com.google.gson.annotations.SerializedName

data class FavoriteBook (

    @field:SerializedName("favoriteId")
    val favoriteId : String? = null,

    @field:SerializedName("bookId")
    val bookId: String? = null,

    @field:SerializedName("username")
    val username: String? = null,

    @field:SerializedName("isFavorite")
    val isFavorite: Boolean? = null
)