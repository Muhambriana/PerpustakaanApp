package com.skripsi.perpustakaanapp.ui.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.models.TempModel
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.responses.BookDeleteResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailBookViewModel(private val repository: LibraryRepository) : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()

    fun deleteBook(token: String, bookId: String) {
        isLoading.value = true
        val tempModel = TempModel(bookId)
        val post = repository.deleteBook(token, tempModel)
        post.enqueue(object : Callback<BookDeleteResponse> {
            override fun onResponse( call: Call<BookDeleteResponse>, response: Response<BookDeleteResponse>) {
                isLoading.value = false
            }

            override fun onFailure(call: Call<BookDeleteResponse>, t: Throwable) {
                isLoading.value = false
                errorMessage.postValue(t.message)
            }
        })
    }
}