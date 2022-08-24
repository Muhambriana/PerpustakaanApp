package com.skripsi.perpustakaanapp.core.apihelper

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
    ): Call<BookResponse>

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
    ):Call<PendingLoanResponse>

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
    ):Call<LoanHistoryResponse>

    @POST("transaction/detail")
    fun getDetailBook(
        @Header("Authorization") token: String,
        @Body bookId: ModelBookId
    ):Call<DetailBookResponse>

//    @POST("book/loan")
//    fun loanBook(
//
//    ):Call<>

//    @POST(book/pendingtask)
//    fun pendingTaskList(
//
//    ):Call<>

}

