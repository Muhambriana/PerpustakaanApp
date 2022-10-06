package com.skripsi.perpustakaanapp.ui.member.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.models.User
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyEvent
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.core.responses.GeneralResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(private val repository: LibraryRepository) : ViewModel() {

    var resourceRegisterUser = MutableLiveData<MyEvent<MyResource<String?>>>()

    fun registerUser(username: String, password: String, firstName: String, lastName: String, roleName: String, email: String, phoneNo: String, address: String, gender: Int? ) {
        resourceRegisterUser.postValue(MyEvent(MyResource.Loading()))
        val user = User(username, password, firstName, lastName, roleName, email, phoneNo, address, gender)
        val post = repository.createUser(user)
        post.enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(
                call: Call<GeneralResponse>,
                response: Response<GeneralResponse>
            ) {
                if (response.body()?.code == 0) {
                    resourceRegisterUser.postValue(MyEvent(MyResource.Success(response.body()?.message)))
                } else {
                    resourceRegisterUser.postValue((MyEvent(MyResource.Error(response.body()?.message))))
                }
            }

            override fun onFailure(
                call: Call<GeneralResponse>,
                t: Throwable)
            {
                resourceRegisterUser.postValue(MyEvent(MyResource.Error(t.message)))
            }
        })
    }

}