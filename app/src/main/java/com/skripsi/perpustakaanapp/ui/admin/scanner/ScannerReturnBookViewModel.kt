package com.skripsi.perpustakaanapp.ui.admin.scanner

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.models.ModelForReturnBook
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyEvent
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.core.responses.GeneralResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal

class ScannerReturnBookViewModel(private val repository: LibraryRepository): ViewModel() {

    val resourceScanner = MutableLiveData<MyEvent<MyResource<GeneralResponse?>>>()
    val resourcePenaltyPayment = MutableLiveData<MyEvent<MyResource<GeneralResponse?>>>()

    fun scannerReturning(token: String, qrCode: String) {
        resourceScanner.postValue(MyEvent(MyResource.Loading()))
        val model = ModelForReturnBook(qrCode, BigDecimal.valueOf(0))
        val post = repository.returningScan(token, model)
        post.enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(
                call: Call<GeneralResponse>,
                response: Response<GeneralResponse>
            ) {
                if (response.body()?.code == 0 || response.body()?.code == -3) {
                    resourceScanner.postValue(MyEvent(MyResource.Success(response.body())))
                } else {
                    resourceScanner.postValue(MyEvent(MyResource.Error(response.body()?.message)))
                }
            }

            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                resourceScanner.postValue(MyEvent(MyResource.Error(t.message)))
            }
        })
    }

    fun scannerOverdueReturning(token: String, qrCode: String, money: BigDecimal?) {
        resourcePenaltyPayment.postValue(MyEvent(MyResource.Loading()))
        val model = ModelForReturnBook(qrCode, money)
        val post = repository.returningScan(token, model)
        post.enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(
                call: Call<GeneralResponse>,
                response: Response<GeneralResponse>
            ) {
                if (response.body()?.code == 0 || response.body()?.code == -3) {
                    resourcePenaltyPayment.postValue(MyEvent(MyResource.Success(response.body())))
                } else {
                    resourcePenaltyPayment.postValue(MyEvent(MyResource.Error(response.body()?.message)))
                }
            }

            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                resourcePenaltyPayment.postValue(MyEvent(MyResource.Error(t.message)))
            }
        })
    }
}