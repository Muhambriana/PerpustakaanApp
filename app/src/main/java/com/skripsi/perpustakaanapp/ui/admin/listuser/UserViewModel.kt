package com.skripsi.perpustakaanapp.ui.admin.listuser

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

    fun getAllMember(token: String) {
        resourceMember.postValue(MyEvent(MyResource.Loading()))
        val response = repository.getAllMember(token)
        response.enqueue(object : Callback<ListUserResponse> {
            override fun onResponse(
                call: Call<ListUserResponse>,
                response: Response<ListUserResponse>
            ) {
                if (response.body()?.code == 0) {
                    resourceMember.postValue(MyEvent(MyResource.Success(response.body()?.userItems)))
                } else {
                    resourceMember.postValue(MyEvent(MyResource.Error(response.body()?.message)))
                }
            }

            override fun onFailure(call: Call<ListUserResponse>, t: Throwable) {
                resourceMember.postValue(MyEvent(MyResource.Error(t.message)))
            }
        })
    }
}