package com.skripsi.perpustakaanapp.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.Event
import com.skripsi.perpustakaanapp.core.resource.Resource
import com.skripsi.perpustakaanapp.core.responses.GeneralResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val repository: LibraryRepository) : ViewModel() {

    val resourceLogout = MutableLiveData<Event<Resource<String?>>>()

    fun userLogout(token: String){
        resourceLogout.postValue(Event(Resource.Loading()))
        val response = repository.userLogout(token)
        response.enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>
            ) {
                if (response.body()?.code == 0) {
                    resourceLogout.postValue(Event(Resource.Success(response.body()?.message)))
                } else {
                    resourceLogout.postValue(Event(Resource.Error(response.body()?.message)))
                }
            }
            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                resourceLogout.postValue(Event(Resource.Error(t.message)))
            }
        })
    }
}