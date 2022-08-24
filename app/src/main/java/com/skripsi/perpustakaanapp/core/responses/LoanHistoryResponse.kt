package com.skripsi.perpustakaanapp.core.responses

import com.google.gson.annotations.SerializedName
import com.skripsi.perpustakaanapp.core.models.LoanHistory

data class LoanHistoryResponse(

    @SerializedName("code")
    val code: Int? = null,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("trxHistoryItems")
    val loanHistoryItems: List<LoanHistory>
)
