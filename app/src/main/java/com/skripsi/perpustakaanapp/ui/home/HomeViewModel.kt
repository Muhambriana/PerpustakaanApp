package com.skripsi.perpustakaanapp.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.responses.LogoutResponse
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val repository: LibraryRepository) : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()
    val isSuccess = MutableLiveData<Boolean?>()

    fun userLogout(token: String){
        isLoading.value = true
        val response = repository.userLogout(token)
        response.enqueue(object : Callback<LogoutResponse> {
            override fun onResponse(call: Call<LogoutResponse>, response: Response<LogoutResponse>
            ) {
                if (response.body()?.code == 0) {
                    isSuccess.value = true
                }
            }

            override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                isLoading.value = false
                errorMessage.postValue(t.message)
            }
        })
    }
}