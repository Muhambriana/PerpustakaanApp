package com.skripsi.perpustakaanapp.core.models

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("token")
    val token: String? = null,

    @field:SerializedName("sessionId")
    val sessionId: String? = null,

    @field:SerializedName("firstName")
    val fullName: String? = null,

    @field:SerializedName("roleName")
    val roleName: String? = null

)
