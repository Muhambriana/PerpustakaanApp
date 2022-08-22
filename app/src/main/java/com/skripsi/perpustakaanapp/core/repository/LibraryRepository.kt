package com.skripsi.perpustakaanapp.core.repository

import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.models.*
import okhttp3.RequestBody

class LibraryRepository constructor(private val retrofitClient: RetrofitClient) {

    fun getAllBooks(token: String) = retrofitClient.create().getAllBook(token)

    fun createBook(token: String, book: Book) = retrofitClient.create().postCreateBook(token, book)

    fun userLogin(nis: String, password: String) = retrofitClient.create().userLogin(nis, password)

    fun userLogout(token: String) = retrofitClient.create().userLogout(token)

    fun createUser(user: User) = retrofitClient.create().createUser(user)

    fun updateBook(token: String, book: Book) = retrofitClient.create().postUpdateBook(token, book)

    fun deleteBook(token: String, bookId: ModelForDelete) = retrofitClient.create().postDeleteBook(token, bookId)

    fun createTransaction(data: ModelForCreateTransaction) = retrofitClient.create().postCreateTransaction(data)

    fun getAllPendingLoan(token: String) = retrofitClient.create().getAllPendingLoan(token)

    fun approveLoan(token: String, pendingLoanId: ModelForApproveLoan) = retrofitClient.create().postApproveLoan(token, pendingLoanId)

}