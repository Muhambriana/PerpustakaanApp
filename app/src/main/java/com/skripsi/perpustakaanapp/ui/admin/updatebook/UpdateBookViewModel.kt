package com.skripsi.perpustakaanapp.ui.admin.updatebook

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.models.Book
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.responses.GeneralResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateBookViewModel(private val repository: LibraryRepository) : ViewModel(){

    val errorMessage = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()
    var failMessage = MutableLiveData<String?>()

    fun updateBook(token: String,bookId: String, title: String, edition: String, author: String, publisher: String, publisherDate: String, copies: String, source: String, remark: String){
        isLoading.value = true
        val book = Book(bookId, title, edition, author, publisher, publisherDate, copies, source, remark)
        val post = repository.updateBook(token, book)
        post.enqueue(object : Callback<GeneralResponse>{
            override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                if (response.body()?.code == 0){
                    isLoading.value = false
                    failMessage.value=""
                }
                else {
                    isLoading.value = false
                    failMessage.value= response.body()?.message
                }
            }

            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                isLoading.value = false
                errorMessage.postValue(t.message)
            }
        })
    }

}