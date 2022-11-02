package com.skripsi.perpustakaanapp.ui.admin.scanner

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.models.ModelForAttendance
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyEvent
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.core.responses.GeneralResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScannerViewModel(private val repository: LibraryRepository): ViewModel() {

    val resourceScanner = MutableLiveData<MyEvent<MyResource<String?>>>()

    fun scannerAttendance(token: String, qrCode: String, admin: String) {
        resourceScanner.postValue(MyEvent(MyResource.Loading()))
        val data = ModelForAttendance(qrCode, admin)
        val post = repository.attendanceScan(token, data)
        post.enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(
                call: Call<GeneralResponse>,
                response: Response<GeneralResponse>
            ) {
                if (response.body()?.code == 0) {
                    resourceScanner.postValue(MyEvent(MyResource.Success(response.body()?.message)))
                } else {
                    resourceScanner.postValue(MyEvent(MyResource.Error(response.body()?.message)))
                }
            }

            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                resourceScanner.postValue(MyEvent(MyResource.Error(t.message)))
            }
        })

    }
}