package com.skripsi.perpustakaanapp.ui.user.book

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.models.Book
import com.skripsi.perpustakaanapp.core.models.BookList
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookViewModel(private val repository: LibraryRepository) : ViewModel() {

    val bookList = MutableLiveData<List<Book>>()
    val errorMessage = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()

    fun getAllBooks(token: String) {
        isLoading.value = true
        val response = repository.getAllBooks(token)
        response.enqueue(object : Callback<BookList> {
            override fun onResponse(call: Call<BookList>, response: Response<BookList>) {
                isLoading.value = false
                bookList.postValue(response.body()?.bookItems)
            }

            override fun onFailure(call: Call<BookList>, t: Throwable) {
                isLoading.value = false
                errorMessage.postValue(t.message)
            }
        })
    }

    companion object {
        private const val TAG = "BookViewModel"
    }
}