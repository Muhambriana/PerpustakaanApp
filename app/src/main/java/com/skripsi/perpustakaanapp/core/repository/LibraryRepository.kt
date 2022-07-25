package com.skripsi.perpustakaanapp.core.repository

import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.models.Book
import com.skripsi.perpustakaanapp.core.models.User

class LibraryRepository constructor(private val retrofitClient: RetrofitClient) {

    fun getAllBooks(token: String) = retrofitClient.create().getAllBook(token)

    fun createBook(token: String, book: Book) = retrofitClient.create().postCreateBook(token, book)

    fun userLogin(nis: String, password: String) = retrofitClient.create().userLogin(nis, password)

    fun userLogout(token: String) = retrofitClient.create().userLogout(token)

    fun createUser(user: User) = retrofitClient.create().createUser(user)


}