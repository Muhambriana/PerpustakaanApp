package com.skripsi.perpustakaanapp.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.response.LoginResponse
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginViewModel(private val repository: LibraryRepository) : ViewModel(){

    val errorMessage = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()
    var failMessage = MutableLiveData<String?>()
    val token = MutableLiveData<String?>()
    val userName = MutableLiveData<String?>()
    val roleName = MutableLiveData<String?>()

    fun userLogin(nis: String, password: String){
        isLoading.value = true
        val loginRequest = repository.userLogin(nis, password)
        loginRequest.enqueue(object: Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                isLoading.value = false
                if (response.body()?.code == 0) {
                    failMessage.value = ""
                    token.value = response.body()?.token
                    userName.value = response.body()?.fullName
                    roleName.value = response.body()?.roleName
                }
                else {
                    failMessage.value = response.body()?.message.toString()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                isLoading.value = false
                errorMessage.postValue(t.message)
            }
        })
    }
}