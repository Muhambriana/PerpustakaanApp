package com.skripsi.perpustakaanapp.core.repository

import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

class LibraryRepository constructor(private val retrofitClient: RetrofitClient) {

    fun getAllBooks(token: String) = retrofitClient.create().getAllBook(token)

    fun createBook(token: String, title: RequestBody, image: MultipartBody.Part?) = retrofitClient.create().postCreateBook(token, title, image)

    fun userLogin(nis: String, password: String) = retrofitClient.create().userLogin(nis, password)

    fun userLogout(token: String) = retrofitClient.create().userLogout(token)

    fun createUser(user: User) = retrofitClient.create().createUser(user)

    fun updateBook(token: String, book: Book) = retrofitClient.create().postUpdateBook(token, book)

    fun deleteBook(token: String, bookId: ModelBookId) = retrofitClient.create().postDeleteBook(token, bookId)

    fun createTransaction(data: ModelForCreateTransaction) = retrofitClient.create().postCreateTransaction(data)

    fun getAllPendingLoan(token: String) = retrofitClient.create().getAllPendingLoan(token)

    fun approveLoan(token: String, data: ModelForApproveAndRejectLoan) = retrofitClient.create().postApproveLoan(token, data)

    fun rejectLoan(token: String, data: ModelForApproveAndRejectLoan) = retrofitClient.create().postRejectLoan(token, data)

    fun getAllLoanHistoryMember(token: String, username: ModelUsername) = retrofitClient.create().getAllLoanHistoryMember(token, username)

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

    fun returningScan(token: String, data: ModelForReturnBook) = retrofitClient.create().returningScan(token, data)
}