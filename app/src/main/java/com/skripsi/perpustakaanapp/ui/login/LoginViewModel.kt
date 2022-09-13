package com.skripsi.perpustakaanapp.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.responses.LoginResponse
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.Event
import com.skripsi.perpustakaanapp.core.resource.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginViewModel(private val repository: LibraryRepository) : ViewModel(){

    val resourceLogin = MutableLiveData<Event<Resource<LoginResponse?>>>()

    fun userLogin(nis: String, password: String){
        resourceLogin.postValue(Event(Resource.Loading()))
        val loginRequest = repository.userLogin(nis, password)
        loginRequest.enqueue(object: Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.body()?.code == 0) {
                    resourceLogin.postValue(Event(Resource.Success(response.body())))
                } else {
                    resourceLogin.postValue(Event(Resource.Error(response.body()?.message)))
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                resourceLogin.postValue(Event(Resource.Error(t.message)))
            }
        })
    }
}

/*
package com.skripsi.perpustakaanapp.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.responses.LoginResponse
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginViewModel(private val repository: LibraryRepository) : ViewModel(){

    val errorMessage = MutableLiveData<String?>()
    val isLoading = MutableLiveData<Boolean>()
    var failMessage = MutableLiveData<String?>()
    val token = MutableLiveData<String?>()
    val firstName = MutableLiveData<String?>()
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
                    firstName.value = response.body()?.fullName
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
 */