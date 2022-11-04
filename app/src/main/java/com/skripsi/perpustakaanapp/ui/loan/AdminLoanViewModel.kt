package com.skripsi.perpustakaanapp.ui.loan

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.models.LoanHistory
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyEvent
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.core.responses.ListLoanHistoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminLoanViewModel(private val repository: LibraryRepository) : ViewModel() {
    val resourceLoan = MutableLiveData<MyEvent<MyResource<List<LoanHistory>>>>()

    fun getAllOngoingLoan(token: String) {
        resourceLoan.postValue(MyEvent(MyResource.Loading()))
        val response = repository.getAllOngoingLoan(token)
        response.enqueue(object : Callback<ListLoanHistoryResponse> {
            override fun onResponse(
                call: Call<ListLoanHistoryResponse>,
                response: Response<ListLoanHistoryResponse>
            ) {
                if (response.body()?.code == 0) {
                    if (response.body()?.loanHistoryItems?.isEmpty() == true) {
                        resourceLoan.postValue(MyEvent(MyResource.Empty()))
                        return
                    }
                    resourceLoan.postValue(MyEvent(MyResource.Success(response.body()?.loanHistoryItems)))
                }
                else {
                    resourceLoan.postValue(MyEvent(MyResource.Error(response.body()?.message)))
                }
            }

            override fun onFailure(
                call: Call<ListLoanHistoryResponse>,
                t: Throwable
            ) {
                resourceLoan.postValue(MyEvent(MyResource.Error(t.message)))
            }
        })
    }

    fun getAllRejectedLoan(token: String) {
        resourceLoan.postValue(MyEvent(MyResource.Loading()))
        val response = repository.getAllRejectedLoan(token)
        response.enqueue(object : Callback<ListLoanHistoryResponse> {
            override fun onResponse(
                call: Call<ListLoanHistoryResponse>,
                response: Response<ListLoanHistoryResponse>
            ) {
                if (response.body()?.code == 0) {
                    if (response.body()?.loanHistoryItems?.isEmpty() == true) {
                        resourceLoan.postValue(MyEvent(MyResource.Empty()))
                        return
                    }
                    resourceLoan.postValue(MyEvent(MyResource.Success(response.body()?.loanHistoryItems)))
                }
                else {
                    resourceLoan.postValue(MyEvent(MyResource.Error(response.body()?.message)))
                }
            }

            override fun onFailure(
                call: Call<ListLoanHistoryResponse>,
                t: Throwable
            ) {
                resourceLoan.postValue(MyEvent(MyResource.Error(t.message)))
            }
        })
    }

    fun getAllOverdueLoan(token: String) {
        resourceLoan.postValue(MyEvent(MyResource.Loading()))
        val response = repository.getAllOverdueLoan(token)
        response.enqueue(object : Callback<ListLoanHistoryResponse> {
            override fun onResponse(
                call: Call<ListLoanHistoryResponse>,
                response: Response<ListLoanHistoryResponse>
            ) {
                if (response.body()?.code == 0) {
                    if (response.body()?.loanHistoryItems?.isEmpty() == true) {
                        resourceLoan.postValue(MyEvent(MyResource.Empty()))
                        return
                    }
                    resourceLoan.postValue(MyEvent(MyResource.Success(response.body()?.loanHistoryItems)))
                }
                else {
                    resourceLoan.postValue(MyEvent(MyResource.Error(response.body()?.message)))
                }
            }

            override fun onFailure(
                call: Call<ListLoanHistoryResponse>,
                t: Throwable
            ) {
                resourceLoan.postValue(MyEvent(MyResource.Error(t.message)))
            }
        })
    }

    fun getAllFinishLoan(token: String) {
        resourceLoan.postValue(MyEvent(MyResource.Loading()))
        val response = repository.getAllFinishLoan(token)
        response.enqueue(object : Callback<ListLoanHistoryResponse> {
            override fun onResponse(
                call: Call<ListLoanHistoryResponse>,
                response: Response<ListLoanHistoryResponse>
            ) {
                if (response.body()?.code == 0) {
                    if (response.body()?.loanHistoryItems?.isEmpty() == true) {
                        resourceLoan.postValue(MyEvent(MyResource.Empty()))
                        return
                    }
                    resourceLoan.postValue(MyEvent(MyResource.Success(response.body()?.loanHistoryItems)))
                }
                else {
                    resourceLoan.postValue(MyEvent(MyResource.Error(response.body()?.message)))
                }
            }

            override fun onFailure(
                call: Call<ListLoanHistoryResponse>,
                t: Throwable
            ) {
                resourceLoan.postValue(MyEvent(MyResource.Error(t.message)))
            }
        })
    }
}