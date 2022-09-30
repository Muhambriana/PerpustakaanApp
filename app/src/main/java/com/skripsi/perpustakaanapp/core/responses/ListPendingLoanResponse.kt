package com.skripsi.perpustakaanapp.core.responses

import com.google.gson.annotations.SerializedName
import com.skripsi.perpustakaanapp.core.models.PendingLoan

data class ListPendingLoanResponse(

    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("pendingTaskItems")
    val pendingLoanItems: List<PendingLoan>? =  null

)
