package com.skripsi.perpustakaanapp.core.repository

import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

class LibraryRepository constructor(private val retrofitClient: RetrofitClient) {

    fun getAllBooks(token: String) = retrofitClient.create().getAllBook(token)

    fun getAllEBooks(token: String) = retrofitClient.create().getAllEBook(token)

    fun createBook(token: String, data: RequestBody, image: MultipartBody.Part?, pdf: MultipartBody.Part?) = retrofitClient.create().postCreateBook(token, data, image, pdf)

    fun userLogin(nis: String, password: String) = retrofitClient.create().userLogin(nis, password)

    fun userLogout(token: String) = retrofitClient.create().userLogout(token)

    fun createUser(user: User) = retrofitClient.create().createUser(user)

    fun updateBook(token: String, book: Book) = retrofitClient.create().postUpdateBook(token, book)

    fun deleteBook(token: String, bookId: ModelBookId) = retrofitClient.create().postDeleteBook(token, bookId)

    fun createTransaction(data: ModelForCreateTransaction) = retrofitClient.create().postCreateTransaction(data)

    fun getAllPendingLoan(token: String) = retrofitClient.create().getAllPendingLoan(token)

    fun getAllPendingLoanByUsername(token: String) = retrofitClient.create().getAllPendingLoanByUsername(token)

    fun approveLoan(token: String, data: ModelForApproveAndRejectLoan) = retrofitClient.create().postApproveLoan(token, data)

    fun rejectLoan(token: String, data: ModelForApproveAndRejectLoan) = retrofitClient.create().postRejectLoan(token, data)

    fun getAllLoanHistoryMember(token: String, username: ModelUsername) = retrofitClient.create().getAllLoanHistoryMember(token, username)

    fun getAllPendingLoanMember(token: String) = retrofitClient.create().getAllPendingLoanMember(token)

    fun getAllOngoingLoanMember(token: String) = retrofitClient.create().getAllOngoingLoanMember(token)

    fun getAllRejectedLoanMember(token: String) = retrofitClient.create().getAllRejectedLoanMember(token)

    fun getAllOverdueLoanMember(token: String) = retrofitClient.create().getAllOverdueLoanMember(token)

    fun getAllFinishLoanMember(token: String) = retrofitClient.create().getAllFinishLoanMember(token)

    fun getAllFinishLoan(token: String) = retrofitClient.create().getAllFinishLoan(token)

    fun getAllOngoingLoan(token: String) = retrofitClient.create().getAllOngoingLoan(token)

    fun getAllRejectedLoan(token: String) = retrofitClient.create().getAllRejectedLoan(token)

    fun getAllOverdueLoan(token: String) = retrofitClient.create().getAllOverdueLoan(token)

    fun getDetailBook(token: String, bookId: ModelBookId) = retrofitClient.create().getDetailBook(token, bookId)

    fun getDetailUser(token: String, username: ModelUsername) = retrofitClient.create().getDetailUser(token, username)

    fun updateImage(token: String, bookId: RequestBody?, username: RequestBody?, image: MultipartBody.Part?) = retrofitClient.create().updateImage(token, bookId, username, image)

    fun getStatusFavorite(token: String, data: ModelForCreateTransaction) = retrofitClient.create().statusFavorite(token, data)

    fun changeStatusFavorite(token: String, data: ModelForChangeStatusFavorite) = retrofitClient.create().changeStatusFavorite(token, data)

    fun getAllFavorite(token: String) = retrofitClient.create().getAllFavorite(token)

    fun searchBook(token: String, title: ModelForSearchBook) = retrofitClient.create().searchBook(token, title)

    fun getAllMember(token: String) = retrofitClient.create().getAllMember(token)

    fun deleteMember(token: String, username: ModelUsername) = retrofitClient.create().deleteMember(token, username)

    fun updateUser(token: String, member: User) = retrofitClient.create().updateUser(token, member)

    fun attendanceScan(token: String, data: ModelForAttendance) = retrofitClient.create().attendanceScan(token, data)

    fun getAllAttendance(token: String) = retrofitClient.create().getAllAttendance(token)

    fun getAllAttendanceMember(token: String) = retrofitClient.create().getAllAttendanceMember(token)

    fun returningScan(token: String, data: ModelForReturnBook) = retrofitClient.create().returningScan(token, data)

    fun showPDF(token: String, fileName: String) = retrofitClient.create().showPDF(token, fileName)

    fun statsAdmin(token: String) = retrofitClient.create().statsAdmin(token)

    fun statsMember(token: String) = retrofitClient.create().statsMember(token)

    fun updateEBook(token: String,  bookId: RequestBody?, pdf: MultipartBody.Part?) = retrofitClient.create().updateEBook(token, bookId, pdf)
}