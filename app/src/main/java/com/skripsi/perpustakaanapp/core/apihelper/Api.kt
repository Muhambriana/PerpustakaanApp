package com.skripsi.perpustakaanapp.core.apihelper

import com.skripsi.perpustakaanapp.core.models.*
import com.skripsi.perpustakaanapp.core.responses.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
    ): Call<ListBookResponse>

//    @POST("book/create")
//    fun postCreateBook(
//        @Header("Authorization") token: String,
//        @Body book: Book
//    ):Call<GeneralResponse>

    @Multipart
    @POST("book/create")
    fun postCreateBook(
        @Header("Authorization") token: String,
        @Part ("data") data: RequestBody?,
        @Part image: MultipartBody.Part?
    ):Call<GeneralResponse>

    @POST("book/update")
    fun postUpdateBook(
        @Header("Authorization") token: String,
        @Body book: Book
    ):Call<GeneralResponse>

    @POST("book/delete")
    fun postDeleteBook(
        @Header("Authorization") token: String,
        @Body bookId: ModelBookId
    ):Call<GeneralResponse>

    @POST("transaction/create")
    fun postCreateTransaction(
        @Body data: ModelForCreateTransaction
    ):Call<GeneralResponse>

    @POST("pendingtask/list")
    fun getAllPendingLoan(
        @Header("Authorization") token: String
    ):Call<ListPendingLoanResponse>

    @POST("pendingtask/approve")
    fun postApproveLoan(
        @Header("Authorization") token: String,
        @Body data: ModelForApproveAndRejectLoan
    ):Call<GeneralResponse>

    @POST("pendingtask/reject")
    fun postRejectLoan(
        @Header("Authorization") token: String,
        @Body data: ModelForApproveAndRejectLoan
    ):Call<GeneralResponse>

    @POST("transaction/history")
    fun getAllLoanHistoryMember(
        @Header("Authorization") token: String,
        @Body username: ModelUsername
    ):Call<ListLoanHistoryResponse>

    @POST("book/detail")
    fun getDetailBook(
        @Header("Authorization") token: String,
        @Body bookId: ModelBookId
    ):Call<DetailBookResponse>

    @POST("user/detail")
    fun getDetailUser(
        @Header("Authorization") token: String,
        @Body username: ModelUsername
    ):Call<DetailUserResponse>

    @Multipart
    @POST("image/update")
    fun updateImage(
        @Header("Authorization") token: String,
        @Part("bookId")  bookId: RequestBody?,
        @Part("username") username: RequestBody?,
        @Part image : MultipartBody.Part?
    ):Call<GeneralResponse>

    @POST("favorite/status")
    fun statusFavorite(
        @Header("Authorization") token: String,
        @Body data: ModelForCreateTransaction
    ):Call<StatusFavoriteResponse>

    @POST("favorite/change")
    fun changeStatusFavorite(
        @Header("Authorization") token: String,
        @Body data: ModelForChangeStatusFavorite
    ):Call<GeneralResponse>

    @POST("favorite/list")
    fun getAllFavorite(
        @Header("Authorization") token: String
    ):Call<ListFavoriteResponse>

    @POST("book/findBookByTitle")
    fun searchBook(
        @Header("Authorization") token: String,
        @Body title: ModelForSearchBook
    ):Call<ListBookResponse>

    @POST("user/list")
    fun getAllMember(
        @Header("Authorization") token: String
    ):Call<ListUserResponse>

    @POST("user/delete")
    fun deleteMember(
        @Header("Authorization") token: String,
        @Body username: ModelUsername
    ):Call<GeneralResponse>

    @POST("user/update")
    fun updateUser(
        @Header("Authorization") token: String,
        @Body data: User
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

