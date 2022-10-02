package com.skripsi.perpustakaanapp.ui.member.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.models.User
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.Event
import com.skripsi.perpustakaanapp.core.resource.Resource
import com.skripsi.perpustakaanapp.core.responses.GeneralResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(private val repository: LibraryRepository) : ViewModel() {

    var resourceRegisterUser = MutableLiveData<Event<Resource<String?>>>()

    fun registerUser(username: String, password: String, firstName: String, lastName: String, roleName: String, email: String, phoneNo: String, address: String, gender: Int? ) {
        resourceRegisterUser.postValue(Event(Resource.Loading()))
        val user = User(username, password, firstName, lastName, roleName, email, phoneNo, address, gender)
        val post = repository.createUser(user)
        post.enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(
                call: Call<GeneralResponse>,
                response: Response<GeneralResponse>
            ) {
                if (response.body()?.code == 0) {
                    resourceRegisterUser.postValue(Event(Resource.Success(response.body()?.message)))
                } else {
                    resourceRegisterUser.postValue((Event(Resource.Error(response.body()?.message))))
                }
            }

            override fun onFailure(
                call: Call<GeneralResponse>,
                t: Throwable)
            {
                resourceRegisterUser.postValue(Event(Resource.Error(t.message)))
            }
        })
    }

}

/*
package com.skripsi.perpustakaanapp.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.models.Book
import com.skripsi.perpustakaanapp.core.models.User
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.Resource
import com.skripsi.perpustakaanapp.core.responses.GeneralResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(private val repository: LibraryRepository) : ViewModel() {

    val isLoading = MutableLiveData<Resource<U>>()
    val errorMessage = MutableLiveData<String?>()
    var responseMessage = MutableLiveData<String?>()

    fun registerUser(nisn: String, password: String, fullName: String, roleName: String, email: String, phoneNo: String, address: String, gender: Int){
        isLoading.value = Resource.Loading()
        val user = User(nisn, password, fullName, roleName, email, phoneNo, address, gender)
        val post = repository.createUser(user)
        post.enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>
            ) {
//                isLoading.value = false
                responseMessage.value = response.body()?.message
            }

            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
//                isLoading.value = false
                errorMessage.postValue(t.message)
            }
        })
    }

}
 */