package com.skripsi.perpustakaanapp.ui.admin.usermanagerial.updateuser

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

class UpdateUserViewModel(private val repository: LibraryRepository) : ViewModel() {

    val resourceUpdateUser = MutableLiveData<Event<Resource<String?>>>()

    fun updateUser(token: String, username: String, firstName: String, lastName: String, email: String, phoneNo: String, address: String, gender: Int) {
        resourceUpdateUser.postValue(Event(Resource.Loading()))
        val data = User(username, null, firstName, lastName, null, email, phoneNo, address, gender)
        val response = repository.updateUser(token, data)
        response.enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(
                call: Call<GeneralResponse>,
                response: Response<GeneralResponse>
            ) {
                if (response.body()?.code == 0) {
                    resourceUpdateUser.postValue(Event(Resource.Success(response.body()?.message)))
                } else {
                    resourceUpdateUser.postValue(Event(Resource.Error(response.body()?.message)))
                }
            }
            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                resourceUpdateUser.postValue(Event(Resource.Error(t.message)))
            }
        })
    }
}