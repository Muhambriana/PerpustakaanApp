package com.skripsi.perpustakaanapp.core.repository

import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.models.*

class LibraryRepository constructor(private val retrofitClient: RetrofitClient) {

    fun getAllBooks(token: String) = retrofitClient.create().getAllBook(token)

    fun createBook(token: String, book: Book) = retrofitClient.create().postCreateBook(token, book)

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

    fun getDetaiUser(token: String, username: ModelUsername) = retrofitClient.create().getDetailUser(token, username)

}