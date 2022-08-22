package com.skripsi.perpustakaanapp.ui.admin.loan.pendingloan

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.models.ModelForApproveLoan
import com.skripsi.perpustakaanapp.core.models.PendingLoan
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.Event
import com.skripsi.perpustakaanapp.core.resource.Resource
import com.skripsi.perpustakaanapp.core.responses.GeneralResponse
import com.skripsi.perpustakaanapp.core.responses.PendingLoanResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PendingLoanViewModel(private val repository: LibraryRepository) : ViewModel() {

    val resourcePendingLoan = MutableLiveData<Event<Resource<List<PendingLoan>>>>()
    val resourceApproveLoan = MutableLiveData<Event<Resource<String?>>>()

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

    fun approveLoans(token: String, pendingLoanId: Int) {
        resourceApproveLoan.postValue(Event(Resource.Loading()))
        val modelForApproveLoan = ModelForApproveLoan(pendingLoanId)
        val response = repository.approveLoan(token, modelForApproveLoan)
        response.enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(
                call: Call<GeneralResponse>,
                response: Response<GeneralResponse>
            ) {
                if (response.body()?.code == 0) {
                    resourceApproveLoan.postValue(Event(Resource.Success(response.body()?.message)))
                }
                else {
                    resourceApproveLoan.postValue(Event(Resource.Error(response.body()?.message)))
                }
            }

            override fun onFailure(
                call: Call<GeneralResponse>,
                t: Throwable
            ) {
                resourceApproveLoan.postValue(Event(Resource.Error(t.message)))
            }
        })
    }

}