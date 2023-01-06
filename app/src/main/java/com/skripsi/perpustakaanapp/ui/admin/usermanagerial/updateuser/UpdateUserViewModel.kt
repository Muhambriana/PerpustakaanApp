package com.skripsi.perpustakaanapp.ui.admin.usermanagerial.updateuser

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

class UpdateUserViewModel(private val repository: LibraryRepository) : ViewModel() {

    val resourceUpdateUser = MutableLiveData<MyEvent<MyResource<String?>>>()

    fun updateUser(token: String, username: String, firstName: String, lastName: String, email: String, phoneNo: String, address: String, gender: Int, educationLevel: String) {
        resourceUpdateUser.postValue(MyEvent(MyResource.Loading()))
        val data = User(username, null, firstName, lastName, null, email, phoneNo, address, gender, null, educationLevel)
        val response = repository.updateUser(token, data)
        response.enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(
                call: Call<GeneralResponse>,
                response: Response<GeneralResponse>
            ) {
                if (response.body()?.code == 0) {
                    resourceUpdateUser.postValue(MyEvent(MyResource.Success(response.body()?.message)))
                } else {
                    resourceUpdateUser.postValue(MyEvent(MyResource.Error(response.body()?.message)))
                }
            }
            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                resourceUpdateUser.postValue(MyEvent(MyResource.Error("Connection Failed")))
//                t.message?.let { Log.e("UpdateUserViewModel", it) }
            }
        })
    }
}