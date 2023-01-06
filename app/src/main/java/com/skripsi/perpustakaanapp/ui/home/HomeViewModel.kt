package com.skripsi.perpustakaanapp.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyEvent
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.core.responses.GeneralResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val repository: LibraryRepository) : ViewModel() {

    val resourceLogout = MutableLiveData<MyEvent<MyResource<String?>>>()

    fun userLogout(token: String){
        resourceLogout.postValue(MyEvent(MyResource.Loading()))
        val response = repository.userLogout(token)
        response.enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>
            ) {
                if (response.body()?.code == 0) {
                    resourceLogout.postValue(MyEvent(MyResource.Success(response.body()?.message)))
                } else {
                    resourceLogout.postValue(MyEvent(MyResource.Error(response.body()?.message)))
                }
            }
            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                resourceLogout.postValue(MyEvent(MyResource.Error("Failed Connection, Check Your Connection")))
//                t.message?.let { Log.e("HomeViewModel", it) }
            }
        })
    }
}