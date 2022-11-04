package com.skripsi.perpustakaanapp.ui.pendingloan

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.models.PendingLoan
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyEvent
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.core.responses.ListPendingLoanResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PendingLoanMemberViewModel(private val repository: LibraryRepository) : ViewModel() {

    val resourcePendingLoan = MutableLiveData<MyEvent<MyResource<List<PendingLoan>>>>()

    fun getAllPendingLoans(token: String) {
        resourcePendingLoan.postValue(MyEvent(MyResource.Loading()))
        val response = repository.getAllPendingLoanByUsername(token)
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
}