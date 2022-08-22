package com.skripsi.perpustakaanapp.core.models

import com.google.gson.annotations.SerializedName

data class User(

    @field:SerializedName("username")
    val username: String? = null,

    @field:SerializedName("password")
    val password: String? = null,

    @field:SerializedName("firstName")
    val firstName: String? = null,

    @field:SerializedName("roleName")
    val roleName: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("phoneNo")
    val phoneNo: String? = null,

    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("gender")
    val gender: Int? = null

)
