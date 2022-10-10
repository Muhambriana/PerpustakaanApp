package com.skripsi.perpustakaanapp.ui.member.listattendance

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.models.Attendance
import com.skripsi.perpustakaanapp.core.models.Book
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyEvent
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.core.responses.ListAttendanceResponse
import com.skripsi.perpustakaanapp.core.responses.ListBookResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AttendanceViewModel(private val repository: LibraryRepository) : ViewModel() {

    val resourceAttendance = MutableLiveData<MyEvent<MyResource<List<Attendance>>>>()

    fun getAllAttendances(token: String) {
        resourceAttendance.postValue(MyEvent(MyResource.Loading()))
        val response = repository.getAllAttendance(token)
        response.enqueue(object : Callback<ListAttendanceResponse> {
            override fun onResponse(
                call: Call<ListAttendanceResponse>,
                response: Response<ListAttendanceResponse>
            )
            {
                if (response.body()?.code == 0) {
                    resourceAttendance.postValue(MyEvent(MyResource.Success(response.body()?.attendanceItems)))
                } else {
                    resourceAttendance.postValue(MyEvent(MyResource.Error(response.body()?.message)))
                }
            }

            override fun onFailure(call: Call<ListAttendanceResponse>, t: Throwable) {
                resourceAttendance.postValue(MyEvent(MyResource.Error(t.message)))
            }
        })
    }
}