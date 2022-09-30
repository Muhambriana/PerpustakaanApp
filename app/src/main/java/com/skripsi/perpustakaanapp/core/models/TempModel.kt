package com.skripsi.perpustakaanapp.core.models

import com.google.gson.annotations.SerializedName

data class ModelBookId(
    @SerializedName("bookId")
    val bookId : String? = null
)

data class ModelForCreateTransaction(
    @SerializedName("username")
    val username : String? = null,

    @SerializedName("bookId")
    val bookId: String? = null
)

data class ModelForApproveAndRejectLoan(
    @SerializedName("pendingTaskId")
    val pendingLoanId: Int,

    @SerializedName("username")
    val adminUsername: String?
)

data class ModelUsername(
    @SerializedName("username")
    val username: String? = null
)

data class ModelForChangeStatusFavorite(

    @field:SerializedName("username")
    val username: String? = null,

    @field:SerializedName("bookId")
    val bookId: String? = null,

    @field:SerializedName("isFavorite")
    val isFavorite: Boolean? = null
)

data class ModelForSearchBook(

    @field:SerializedName("title")
    val title: String? = null
)

