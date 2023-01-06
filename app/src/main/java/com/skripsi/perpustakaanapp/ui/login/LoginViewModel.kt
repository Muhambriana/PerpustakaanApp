package com.skripsi.perpustakaanapp.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyEvent
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.core.responses.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginViewModel(private val repository: LibraryRepository) : ViewModel(){

    val resourceLogin = MutableLiveData<MyEvent<MyResource<LoginResponse?>>>()

    fun userLogin(nis: String, password: String){
        resourceLogin.postValue(MyEvent(MyResource.Loading()))
        val loginRequest = repository.userLogin(nis, password)
        loginRequest.enqueue(object: Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.body()?.code == 0) {
                    resourceLogin.postValue(MyEvent(MyResource.Success(response.body())))
                } else {
                    resourceLogin.postValue(MyEvent(MyResource.Error(response.body()?.message)))
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                resourceLogin.postValue(MyEvent(MyResource.Error("Connection Failed")))
//                t.message?.let { Log.e("LoginViewModel", it) }
            }
        })
    }
}