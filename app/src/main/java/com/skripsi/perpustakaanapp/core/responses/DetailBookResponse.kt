package com.skripsi.perpustakaanapp.core.responses

import com.google.gson.annotations.SerializedName

data class DetailBookResponse(

    @SerializedName("code")
    val code : Int? = null,

    @SerializedName("message")
    val message : String? = null,

    @SerializedName("title")
    val title : String? = null

)

data class DetailUserResponse(

    @SerializedName("code")
    val code : Int? = null,

    @SerializedName("message")
    val message : String? = null,

    @SerializedName("firstName")
    val firstName : String? = null

)
