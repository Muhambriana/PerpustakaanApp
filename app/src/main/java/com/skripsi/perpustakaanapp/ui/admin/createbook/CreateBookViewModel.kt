package com.skripsi.perpustakaanapp.ui.admin.createbook

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.models.Book
import com.skripsi.perpustakaanapp.core.responses.BookCreateResponse
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateBookViewModel(private val repository: LibraryRepository) : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()
    val failMessage = MutableLiveData<String>()

    fun createBook(token: String, title: String, edition: String, author: String, publisher: String, publisherDate: String, copies: String, source: String, remark: String){
        isLoading.value = true
        val book = Book(title, edition, author, publisher, publisherDate, copies, source, remark)
        val post = repository.createBook(token, book)
        post.enqueue(object : Callback<BookCreateResponse> {
            override fun onResponse(call: Call<BookCreateResponse>, response: Response<BookCreateResponse>) {
                isLoading.value = false
                if (response.body()?.code ==0){
                    failMessage.value = ""
                }
                else {
                    //mengirim pesan gagal ke activity
                    failMessage.value = response.body()?.message
                }
            }

            override fun onFailure(call: Call<BookCreateResponse>, t: Throwable) {
                isLoading.value = false
                errorMessage.postValue(t.message)
            }
        })
    }

}