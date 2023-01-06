package com.skripsi.perpustakaanapp.ui.statistik

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.models.StatisticAdminModel
import com.skripsi.perpustakaanapp.core.models.StatisticMemberModel
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyEvent
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.core.responses.StatisticResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StatisticViewModel(private val repository: LibraryRepository) : ViewModel() {

    val resourceStatisticAdmin = MutableLiveData<MyEvent<MyResource<StatisticAdminModel>>>()
    val resourceStatisticMember = MutableLiveData<MyEvent<MyResource<StatisticMemberModel>>>()

    fun getStatisticAdmin(token: String) {
        resourceStatisticAdmin.postValue(MyEvent(MyResource.Loading()))
        val response = repository.statsAdmin(token)
        response.enqueue(object : Callback<StatisticResponse> {
            override fun onResponse(
                call: Call<StatisticResponse>,
                response: Response<StatisticResponse>
            ) {
                if (response.body()?.code == 0) {
                    resourceStatisticAdmin.postValue(MyEvent(MyResource.Success(response.body()?.statisticAdmin)))
                }
                else {
                    resourceStatisticAdmin.postValue(MyEvent(MyResource.Error(response.body()?.message)))
                }
            }

            override fun onFailure(
                call: Call<StatisticResponse>,
                t: Throwable
            ) {
                resourceStatisticAdmin.postValue(MyEvent(MyResource.Error("Failed Connection, Check Your Connection")))
//                t.message?.let { Log.e("StatisticViewModel", it) }
            }
        })
    }

    fun getStatisticMember(token: String) {
        resourceStatisticMember.postValue(MyEvent(MyResource.Loading()))
        val response = repository.statsMember(token)
        response.enqueue(object : Callback<StatisticResponse> {
            override fun onResponse(
                call: Call<StatisticResponse>,
                response: Response<StatisticResponse>
            ) {
                if (response.body()?.code == 0) {
                    resourceStatisticMember.postValue(MyEvent(MyResource.Success(response.body()?.statisticMember)))
                }
                else {
                    resourceStatisticMember.postValue(MyEvent(MyResource.Error(response.body()?.message)))
                }
            }

            override fun onFailure(
                call: Call<StatisticResponse>,
                t: Throwable
            ) {
                resourceStatisticMember.postValue(MyEvent(MyResource.Error("Failed Connection, Check Your Connection")))
//                t.message?.let { Log.e("StatisticViewModel", it) }
            }
        })
    }
}