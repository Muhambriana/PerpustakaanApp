package com.skripsi.perpustakaanapp.ui.admin.usermanagerial.scanattendance

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.models.ModelForAttendance
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.Event
import com.skripsi.perpustakaanapp.core.resource.Resource
import com.skripsi.perpustakaanapp.core.responses.GeneralResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScannerViewModel(private val repository: LibraryRepository): ViewModel() {

    val resourceScanner = MutableLiveData<Event<Resource<String?>>>()

    fun scannerAttendance(token: String, qrCode: String, purpose: Int?) {
        resourceScanner.postValue(Event(Resource.Loading()))
        val data = ModelForAttendance(qrCode)
        val post =
            if (purpose == 0) {
                repository.attendanceIn(token, data)
            } else {
                repository.attendanceOut(token, data)
            }
        post.enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(
                call: Call<GeneralResponse>,
                response: Response<GeneralResponse>
            ) {
                if (response.body()?.code == 0) {
                    resourceScanner.postValue(Event(Resource.Success(response.body()?.message)))
                } else {
                    resourceScanner.postValue(Event(Resource.Error(response.body()?.message)))
                }
            }

            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                resourceScanner.postValue(Event(Resource.Error(t.message)))
            }
        })

    }
}