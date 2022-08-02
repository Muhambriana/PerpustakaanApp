package com.skripsi.perpustakaanapp.ui.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.models.TempModel
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.responses.GeneralResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailBookViewModel(private val repository: LibraryRepository) : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()
    val failMessage = MutableLiveData<String?>()

    fun deleteBook(token: String, bookId: String) {
        isLoading.value = true
        val tempModel = TempModel(bookId)
        val post = repository.deleteBook(token, tempModel)
        post.enqueue(object : Callback<GeneralResponse> {
            override fun onResponse( call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                isLoading.value = false
                if (response.body()?.code == 0){
                    failMessage.value = ""
                }
                else {
                    failMessage.value = response.body()?.message
                }
            }

            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                isLoading.value = false
                errorMessage.postValue(t.message)
            }
        })
    }
}