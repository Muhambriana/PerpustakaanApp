package com.skripsi.perpustakaanapp.ui.admin.listuser

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.models.ModelUsername
import com.skripsi.perpustakaanapp.core.models.User
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyEvent
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.core.responses.ListUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel(private val repository: LibraryRepository) : ViewModel() {

    val resourceMember = MutableLiveData<MyEvent<MyResource<List<User>>>>()
    val resourceSearchMember = MutableLiveData<MyEvent<MyResource<List<User>>>>()

    fun getAllMember(token: String) {
        resourceMember.postValue(MyEvent(MyResource.Loading()))
        val response = repository.getAllMember(token)
        response.enqueue(object : Callback<ListUserResponse> {
            override fun onResponse(
                call: Call<ListUserResponse>,
                response: Response<ListUserResponse>
            ) {
                if (response.body()?.code == 0) {
                    if (response.body()?.userItems?.isEmpty() == true) {
                        resourceMember.postValue(MyEvent(MyResource.Empty()))
                        return
                    }
                    resourceMember.postValue(MyEvent(MyResource.Success(response.body()?.userItems)))
                } else {
                    resourceMember.postValue(MyEvent(MyResource.Error(response.body()?.message)))
                }
            }

            override fun onFailure(call: Call<ListUserResponse>, t: Throwable) {
                resourceMember.postValue(MyEvent(MyResource.Error("Failed Connection, Check Your Connection")))
//                t.message?.let { Log.e("UserViewModel", it) }
            }
        })
    }

    fun searchMember(token: String, username: String?) {
        resourceSearchMember.postValue(MyEvent(MyResource.Loading()))
        val data = ModelUsername(username)
        val response = repository.findUser(token, data)
        response.enqueue(object : Callback<ListUserResponse> {
            override fun onResponse(
                call: Call<ListUserResponse>,
                response: Response<ListUserResponse>
            ) {
                if (response.body()?.code == 0) {
                    if (response.body()?.userItems?.isEmpty() == true) {
                        resourceSearchMember.postValue(MyEvent(MyResource.Empty()))
                        return
                    }
                    resourceSearchMember.postValue(MyEvent(MyResource.Success(response.body()?.userItems)))
                } else {
                    resourceSearchMember.postValue(MyEvent(MyResource.Error(response.body()?.message)))
                }
            }

            override fun onFailure(call: Call<ListUserResponse>, t: Throwable) {
                resourceSearchMember.postValue(MyEvent(MyResource.Error("Failed Connection, Server Under Maintenance")))
//                t.message?.let { Log.e("UserViewModel", it) }
            }
        })
    }
}