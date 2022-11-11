package com.skripsi.perpustakaanapp.core.responses

import com.google.gson.annotations.SerializedName

data class DetailBookResponse(

    @SerializedName("code")
    val code : Int? = null,

    @SerializedName("message")
    val message : String? = null,

    @field:SerializedName("id")
    val bookId: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("author")
    val author: String? = null,

    @field:SerializedName("publisher")
    val publisher: String? = null,

    @field:SerializedName("yearOfPublication")
    val publisherDate: String? = null,

    @field:SerializedName("stock")
    val stock: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("imageUrl")
    val imageUrl: String? = null,

    @field:SerializedName("ebook")
    val eBook: String? = null

)

data class DetailUserResponse(

    @SerializedName("code")
    val code : Int? = null,

    @SerializedName("message")
    val message : String? = null,

    @field:SerializedName("username")
    val username: String? = null,

    @field:SerializedName("firstName")
    val firstName: String? = null,

    @field:SerializedName("lastName")
    val lastName: String? = null,

    @field:SerializedName("roleName")
    val roleName: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("phoneNo")
    val phoneNo: String? = null,

    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("gender")
    val gender: Int? = null,

    @field:SerializedName("avatar")
    var avatar: String? = null

)
