package com.skripsi.perpustakaanapp.ui.book.detailbook

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.models.Book
import com.skripsi.perpustakaanapp.core.models.ModelForCreateTransaction
import com.skripsi.perpustakaanapp.core.models.ModelBookId
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.Event
import com.skripsi.perpustakaanapp.core.resource.Resource
import com.skripsi.perpustakaanapp.core.responses.DetailBookResponse
import com.skripsi.perpustakaanapp.core.responses.GeneralResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailBookViewModel(private val repository: LibraryRepository) : ViewModel() {

    val resourceDeleteBook = MutableLiveData<Event<Resource<String?>>>()
    val resourceLoanBook = MutableLiveData<Event<Resource<String?>>>()
    val resourceDetailBook = MutableLiveData<Event<Resource<Book>>>()


    fun deleteBook(token: String, bookId: String) {
        resourceDeleteBook.postValue(Event(Resource.Loading()))
        val modelBookId = ModelBookId(bookId)
        val post = repository.deleteBook(token, modelBookId)
        post.enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(
                call: Call<GeneralResponse>,
                response: Response<GeneralResponse>,
            ) {
                if (response.body()?.code == 0) {
                    resourceDeleteBook.postValue(Event(Resource.Success(response.body()?.message)))
                } else {
                    resourceDeleteBook.postValue(Event(Resource.Error(response.body()?.message)))
                }
            }

            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                resourceDeleteBook.postValue(Event(Resource.Error(t.message)))
            }
        })
    }

    fun createTransaction(userName: String?, bookId: String?) {
        resourceLoanBook.postValue(Event(Resource.Loading()))
        val modelForCreateTransaction = ModelForCreateTransaction(userName, bookId)
        val post = repository.createTransaction(modelForCreateTransaction)
        post.enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(
                call: Call<GeneralResponse>,
                response: Response<GeneralResponse>,
            ) {
                if (response.body()?.code == 0) {
                    resourceLoanBook.postValue(Event(Resource.Success(response.body()?.message)))
                } else {
                    resourceLoanBook.postValue(Event(Resource.Error(response.message())))
                }
            }

            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                resourceLoanBook.postValue(Event(Resource.Error(t.message)))
            }
        })
    }

    fun getDetailBook(token: String, bookId: String) {
        resourceDetailBook.postValue(Event(Resource.Loading()))
        val modelBookId = ModelBookId(bookId)
        val response = repository.getDetailBook(token, modelBookId)
        response.enqueue(object : Callback<DetailBookResponse> {
            override fun onResponse(
                call: Call<DetailBookResponse>,
                response: Response<DetailBookResponse>
            ) {
                if (response.body()?.code == 0) {
                    val book = Book(null, response.body()?.title)
                    resourceDetailBook.postValue(Event(Resource.Success(book)))
                }
                else {
                    resourceDetailBook.postValue((Event(Resource.Error(response.body()?.message))))
                }
            }

            override fun onFailure( call: Call<DetailBookResponse>, t: Throwable) {
                resourceDetailBook.postValue(Event(Resource.Error(t.message)))
            }
        })
    }
}