package com.skripsi.perpustakaanapp.ui.member.loanhistory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.models.Book
import com.skripsi.perpustakaanapp.core.models.LoanHistory
import com.skripsi.perpustakaanapp.core.models.ModelBookId
import com.skripsi.perpustakaanapp.core.models.ModelUsername
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.Event
import com.skripsi.perpustakaanapp.core.resource.Resource
import com.skripsi.perpustakaanapp.core.responses.DetailBookResponse
import com.skripsi.perpustakaanapp.core.responses.LoanHistoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoanHistoryViewModel(private val repository: LibraryRepository) : ViewModel() {

    val resourceHistoryLoan = MutableLiveData<Event<Resource<List<LoanHistory>>>>()

    fun getAllLoanHistories(token: String, username: String) {
        resourceHistoryLoan.postValue(Event(Resource.Loading()))
        val modelUsername = ModelUsername(username)
        val response = repository.getAllLoanHistoryMember(token, modelUsername)
        response.enqueue(object : Callback<LoanHistoryResponse> {
            override fun onResponse(
                call: Call<LoanHistoryResponse>,
                response: Response<LoanHistoryResponse>
            ) {
                if (response.body()?.code == 0) {
                    resourceHistoryLoan.postValue(Event(Resource.Success(response.body()?.loanHistoryItems)))
                }
                else {
                    resourceHistoryLoan.postValue(Event(Resource.Error(response.body()?.message)))
                }
            }

            override fun onFailure(
                call: Call<LoanHistoryResponse>,
                t: Throwable
            ) {
                resourceHistoryLoan.postValue(Event(Resource.Error(t.message)))
            }
        })
    }

}