package com.skripsi.perpustakaanapp.core.apihelper

import com.skripsi.perpustakaanapp.core.models.*
import com.skripsi.perpustakaanapp.core.responses.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

@JvmSuppressWildcards
interface Api {

    @POST("user/create")
    fun createAdmin(
        @Header("Authorization") token: String,
        @Body user: User
    ): Call<GeneralResponse>

    @POST("user/create")
    fun registerMember(
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

    @POST("book/list/ebook")
    fun getAllEBook(
        @Header("Authorization") token: String
    ): Call<ListBookResponse>

    @Multipart
    @POST("book/create")
    fun postCreateBook(
        @Header("Authorization") token: String,
        @Part ("data") data: RequestBody?,
        @Part image: MultipartBody.Part?,
        @Part pdfFile: MultipartBody.Part?
    ):Call<GeneralResponse>

    @POST("book/update")
    fun postUpdateBook(
        @Header("Authorization") token: String,
        @Body book: Book
    ):Call<GeneralResponse>

    @HTTP(method = "DELETE", path = "book/delete", hasBody = true)
    fun postDeleteBook(
        @Header("Authorization") token: String,
        @Body bookId: ModelBookId
    ):Call<GeneralResponse>

    @POST("transaction/create")
    fun postCreateTransaction(
        @Header("Authorization") token: String,
        @Body data: ModelForCreateTransaction
    ):Call<GeneralResponse>

    @POST("pendingtask/list")
    fun getAllPendingLoan(
        @Header("Authorization") token: String
    ):Call<ListPendingLoanResponse>

    @POST("pendingtask/list/byusername")
    fun getAllPendingLoanByUsername(
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

    @POST("transaction/pending/member")
    fun getAllPendingLoanMember(
        @Header("Authorization") token: String
    ):Call<ListLoanHistoryResponse>

    @POST("transaction/ongoing/member")
    fun getAllOngoingLoanMember(
        @Header("Authorization") token: String
    ):Call<ListLoanHistoryResponse>

    @POST("transaction/rejected/member")
    fun getAllRejectedLoanMember(
        @Header("Authorization") token: String
    ):Call<ListLoanHistoryResponse>

    @POST("transaction/overdue/member")
    fun getAllOverdueLoanMember(
        @Header("Authorization") token: String
    ):Call<ListLoanHistoryResponse>

    @POST("transaction/finish/member")
    fun getAllFinishLoanMember(
        @Header("Authorization") token: String
    ):Call<ListLoanHistoryResponse>

    @POST("transaction/finish/admin")
    fun getAllFinishLoan(
        @Header("Authorization") token: String
    ):Call<ListLoanHistoryResponse>

    @POST("transaction/ongoing/admin")
    fun getAllOngoingLoan(
        @Header("Authorization") token: String
    ):Call<ListLoanHistoryResponse>

    @POST("transaction/rejected/admin")
    fun getAllRejectedLoan(
        @Header("Authorization") token: String
    ):Call<ListLoanHistoryResponse>

    @POST("transaction/overdue/admin")
    fun getAllOverdueLoan(
        @Header("Authorization") token: String
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
    @POST("media/update/image")
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

    @DELETE("user/delete")
    fun deleteMember(
        @Header("Authorization") token: String,
        @Body username: ModelUsername
    ):Call<GeneralResponse>

    @POST("user/update")
    fun updateUser(
        @Header("Authorization") token: String,
        @Body data: User
    ):Call<GeneralResponse>

    @POST("user/findUserByUsername")
    fun findUser(
        @Header("Authorization") token: String,
        @Body username: ModelUsername
    ):Call<ListUserResponse>

    @POST("attendance/scan")
    fun attendanceScan(
        @Header("Authorization") token: String,
        @Body qrCode: ModelForAttendance
    ):Call<GeneralResponse>

    @POST("attendance/list")
    fun getAllAttendance(
        @Header("Authorization") token: String
    ):Call<ListAttendanceResponse>

    @POST("attendance/list/member")
    fun getAllAttendanceMember(
        @Header("Authorization") token: String
    ):Call<ListAttendanceResponse>

    @POST("transaction/return")
    fun returningScan(
        @Header("Authorization") token: String,
        @Body data: ModelForReturnBook
    ):Call<GeneralResponse>

    @GET("media/eBook/show/{name}")
    fun showPDF(
        @Header("Authorization") token: String,
        @Path("name") fileName: String
    ):Call<ResponseBody>

    @POST("statistic/admin")
    fun statsAdmin(
        @Header("Authorization") token: String
    ):Call<StatisticResponse>

    @POST("statistic/member")
    fun statsMember(
        @Header("Authorization") token: String
    ):Call<StatisticResponse>

    @Multipart
    @POST("media/update/pdf")
    fun updateEBook(
        @Header("Authorization") token: String,
        @Part("bookId")  bookId: RequestBody?,
        @Part pdf: MultipartBody.Part?
    ):Call<GeneralResponse>

    @POST("category/create")
    fun createCategory(
        @Header("Authorization") token: String,
        @Body data: BookCategory
    ):Call<GeneralResponse>

    @POST("category/list")
    fun getAllCategory(
        @Header("Authorization") token: String
    ):Call<ListCategoryResponse>
}

