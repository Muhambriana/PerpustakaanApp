package com.skripsi.perpustakaanapp.ui.admin.loan.pendingloan

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.models.PendingLoan
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.Event
import com.skripsi.perpustakaanapp.core.resource.Resource
import com.skripsi.perpustakaanapp.core.responses.PendingLoanResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PendingLoanViewModel(private val repository: LibraryRepository) : ViewModel() {

    val resourcePendingLoan = MutableLiveData<Event<Resource<List<PendingLoan>>>>()

    fun getAllPendingLoans(token: String) {
        resourcePendingLoan.postValue(Event(Resource.Loading()))
        val response = repository.getAllPendingLoan(token)
        response.enqueue(object : Callback<PendingLoanResponse> {
            override fun onResponse(
                call: Call<PendingLoanResponse>,
                response: Response<PendingLoanResponse>
            ) {
                if (response.body()?.code == 0) {
                    resourcePendingLoan.postValue(Event(Resource.Success(response.body()?.pendingLoanItems)))
                }
                else {
                    resourcePendingLoan.postValue(Event(Resource.Error(response.body()?.message)))
                }
            }

            override fun onFailure(
                call: Call<PendingLoanResponse>,
                t: Throwable
            ) {
                resourcePendingLoan.postValue((Event(Resource.Error(t.message))))
            }
        })
    }

}