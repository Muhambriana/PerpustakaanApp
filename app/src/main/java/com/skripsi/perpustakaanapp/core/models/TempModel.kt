package com.skripsi.perpustakaanapp.core.models

import com.google.gson.annotations.SerializedName

data class ModelForDelete(
    @SerializedName("id")
    val bookId : String? = null
)

data class ModelForCreateTransaction(
    @SerializedName("username")
    val userName : String? = null,

    @SerializedName("bookId")
    val bookId: String? = null
)

data class ModelForApproveAndRejectLoan(
    @SerializedName("pendingTaskId")
    val pendingLoanId: Int,

    @SerializedName("username")
    val adminUsername: String?
)
