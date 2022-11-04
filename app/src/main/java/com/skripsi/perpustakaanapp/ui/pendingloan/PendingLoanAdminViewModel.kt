package com.skripsi.perpustakaanapp.ui.pendingloan

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.models.ModelForApproveAndRejectLoan
import com.skripsi.perpustakaanapp.core.models.PendingLoan
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyEvent
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.core.responses.GeneralResponse
import com.skripsi.perpustakaanapp.core.responses.ListPendingLoanResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PendingLoanAdminViewModel(private val repository: LibraryRepository) : ViewModel() {

    val resourcePendingLoan = MutableLiveData<MyEvent<MyResource<List<PendingLoan>>>>()
    val resourceApproveLoan = MutableLiveData<MyEvent<MyResource<String?>>>()
    val resourceRejectLoan = MutableLiveData<MyEvent<MyResource<String?>>>()


    fun getAllPendingLoans(token: String) {
        resourcePendingLoan.postValue(MyEvent(MyResource.Loading()))
        val response = repository.getAllPendingLoan(token)
        response.enqueue(object : Callback<ListPendingLoanResponse> {
            override fun onResponse(
                call: Call<ListPendingLoanResponse>,
                response: Response<ListPendingLoanResponse>
            ) {
                if (response.body()?.code == 0) {
                    if (response.body()?.pendingLoanItems?.isEmpty() == true) {
                        resourcePendingLoan.postValue(MyEvent(MyResource.Empty()))
                        return
                    }
                    resourcePendingLoan.postValue(MyEvent(MyResource.Success(response.body()?.pendingLoanItems)))
                }
                else {
                    resourcePendingLoan.postValue(MyEvent(MyResource.Error(response.body()?.message)))
                }
            }

            override fun onFailure(
                call: Call<ListPendingLoanResponse>,
                t: Throwable
            ) {
                resourcePendingLoan.postValue((MyEvent(MyResource.Error(t.message))))
            }
        })
    }

    fun approveLoan(token: String, pendingLoanId: Int, adminUsername: String) {
        resourceApproveLoan.postValue(MyEvent(MyResource.Loading()))
        val modelForApproveLoan = ModelForApproveAndRejectLoan(pendingLoanId, adminUsername)
        val response = repository.approveLoan(token, modelForApproveLoan)
        response.enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(
                call: Call<GeneralResponse>,
                response: Response<GeneralResponse>
            ) {
                if (response.body()?.code == 0) {
                    resourceApproveLoan.postValue(MyEvent(MyResource.Success(response.body()?.message)))
                }
                else {
                    resourceApproveLoan.postValue(MyEvent(MyResource.Error(response.body()?.message)))
                }
            }

            override fun onFailure(
                call: Call<GeneralResponse>,
                t: Throwable
            ) {
                resourceApproveLoan.postValue(MyEvent(MyResource.Error(t.message)))
            }
        })
    }

    fun rejectLoan(token: String, pendingLoanId: Int, adminUsername: String) {
        resourceRejectLoan.postValue(MyEvent(MyResource.Loading()))
        val modelForRejectLoan = ModelForApproveAndRejectLoan(pendingLoanId, adminUsername)
        val response = repository.rejectLoan(token, modelForRejectLoan)
        response.enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(
                call: Call<GeneralResponse>,
                response: Response<GeneralResponse>
            ) {
                if (response.body()?.code == 0) {
                    resourceRejectLoan.postValue(MyEvent(MyResource.Success(response.body()?.message)))
                }
                else {
                    resourcePendingLoan.postValue(MyEvent(MyResource.Error(response.body()?.message)))
                }
            }

            override fun onFailure( call: Call<GeneralResponse>, t: Throwable) {
                resourceRejectLoan.postValue(MyEvent(MyResource.Error(t.message)))
            }
        })
    }

}