package com.skripsi.perpustakaanapp.core.responses

import com.google.gson.annotations.SerializedName

data class CreateTransactionResponse(

    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("message")
    val message: String? = null,

//    @field:SerializedName("us"){
//
//    }
)
