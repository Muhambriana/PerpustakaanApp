package com.skripsi.perpustakaanapp.core.models

import com.google.gson.annotations.SerializedName

data class ModelForDelete(
    @SerializedName("id")
    val bookId : String? = null
)

data class ModelForCreateTransaction(
    @SerializedName("userId")
    val userName : String? = null,

    @SerializedName("bookId")
    val bookId: String? = null
)
