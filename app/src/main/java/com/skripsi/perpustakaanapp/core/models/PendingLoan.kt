package com.skripsi.perpustakaanapp.core.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PendingLoan(

    @field:SerializedName("pendingTaskId")
    val pendingLoanId: Int? = null,

    @field:SerializedName("bookTitle")
    val bookTitle: String? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("bookId")
    val bookId: String? = null,

    @field:SerializedName("createdBy")
    val createdBy: String? = null

):Parcelable
