package com.skripsi.perpustakaanapp.ui.member.loanhistory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.models.LoanHistory
import com.skripsi.perpustakaanapp.core.models.ModelUsername
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyEvent
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.core.responses.ListLoanHistoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoanHistoryViewModel(private val repository: LibraryRepository) : ViewModel() {

    val resourceHistoryLoan = MutableLiveData<MyEvent<MyResource<List<LoanHistory>>>>()

    fun getAllLoanHistories(token: String, username: String) {
        resourceHistoryLoan.postValue(MyEvent(MyResource.Loading()))
        val modelUsername = ModelUsername(username)
        val response = repository.getAllLoanHistoryMember(token, modelUsername)
        response.enqueue(object : Callback<ListLoanHistoryResponse> {
            override fun onResponse(
                call: Call<ListLoanHistoryResponse>,
                response: Response<ListLoanHistoryResponse>
            ) {
                if (response.body()?.code == 0) {
                    resourceHistoryLoan.postValue(MyEvent(MyResource.Success(response.body()?.loanHistoryItems)))
                }
                else {
                    resourceHistoryLoan.postValue(MyEvent(MyResource.Error(response.body()?.message)))
                }
            }

            override fun onFailure(
                call: Call<ListLoanHistoryResponse>,
                t: Throwable
            ) {
                resourceHistoryLoan.postValue(MyEvent(MyResource.Error("Failed Connection, Check Your Connection")))
//                t.message?.let { Log.e("LoanHistoryViewModel", it) }
            }
        })
    }

}