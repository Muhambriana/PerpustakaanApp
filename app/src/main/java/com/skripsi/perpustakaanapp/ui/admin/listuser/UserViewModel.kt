package com.skripsi.perpustakaanapp.ui.admin.listuser

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.models.User
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.Event
import com.skripsi.perpustakaanapp.core.resource.Resource
import com.skripsi.perpustakaanapp.core.responses.ListUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel(private val repository: LibraryRepository) : ViewModel() {

    val resourceMember = MutableLiveData<Event<Resource<List<User>>>>()

    fun getAllMember(token: String) {
        resourceMember.postValue(Event(Resource.Loading()))
        val response = repository.getAllMember(token)
        response.enqueue(object : Callback<ListUserResponse> {
            override fun onResponse(
                call: Call<ListUserResponse>,
                response: Response<ListUserResponse>
            ) {
                if (response.body()?.code == 0) {
                    resourceMember.postValue(Event(Resource.Success(response.body()?.userItems)))
                } else {
                    resourceMember.postValue(Event(Resource.Error(response.body()?.message)))
                }
            }

            override fun onFailure(call: Call<ListUserResponse>, t: Throwable) {
                resourceMember.postValue(Event(Resource.Error(t.message)))
            }
        })
    }
}