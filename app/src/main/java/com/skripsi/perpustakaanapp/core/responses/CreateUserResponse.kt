package com.skripsi.perpustakaanapp.core.responses

import com.google.gson.annotations.SerializedName

data class CreateUserResponse(

    @field:SerializedName("code")
    val code: Int,
    @field:SerializedName("message")
    val message: String

)
