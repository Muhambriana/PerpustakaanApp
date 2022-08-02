package com.skripsi.perpustakaanapp.core.apihelper

import com.google.gson.annotations.SerializedName
import com.skripsi.perpustakaanapp.core.models.*
import com.skripsi.perpustakaanapp.core.responses.*
import retrofit2.Call
import retrofit2.http.*

@JvmSuppressWildcards
interface Api {

    @POST("user/create")
    fun createUser(
        @Body user: User
    ): Call<GeneralResponse>

    @Headers("Content-Type: application/json")
    @POST("login")
    fun userLogin(
        @Header("username") nis: String,
        @Header("password") password: String
    ): Call<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST("logout")
    fun userLogout(
        @Header("Authorization") token: String
    ): Call<GeneralResponse>

    @POST("book/list")
    fun getAllBook(
        @Header("Authorization") token: String
    ): Call<BookList>

    @POST("book/create")
    fun postCreateBook(
        @Header("Authorization") token: String,
        @Body book: Book
    ):Call<GeneralResponse>

    @POST("book/update")
    fun postUpdateBook(
        @Header("Authorization") token: String,
        @Body book: Book
    ):Call<GeneralResponse>

    @POST("book/delete")
    fun deleteBook(
        @Header("Authorization") token: String,
        @Body bookId: TempModel
    ):Call<GeneralResponse>

//    @POST("book/loan")
//    fun loanBook(
//
//    ):Call<>

//    @POST(book/pendingtask)
//    fun pendingTaskList(
//
//    ):Call<>

}

