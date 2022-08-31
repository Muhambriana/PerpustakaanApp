package com.skripsi.perpustakaanapp.core.models

import com.google.gson.annotations.SerializedName

data class LoanHistory(

    @SerializedName("bookId")
    val bookId: String? = null,

    @SerializedName("bookTitle")
    val bookTitle: String? = null,

    @SerializedName("releaseDate")
    val releaseDate: String? = null,

    @SerializedName("dueDate")
    val dueDate: String? = null,

    @SerializedName("status")
    val status: String? = null,

    @SerializedName("managedBy")
    val managedBy: String? = null
)
