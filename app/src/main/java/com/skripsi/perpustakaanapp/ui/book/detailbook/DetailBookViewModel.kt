package com.skripsi.perpustakaanapp.ui.book.detailbook

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.models.Book
import com.skripsi.perpustakaanapp.core.models.ModelForCreateTransaction
import com.skripsi.perpustakaanapp.core.models.ModelBookId
import com.skripsi.perpustakaanapp.core.models.ModelForChangeStatusFavorite
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.Event
import com.skripsi.perpustakaanapp.core.resource.Resource
import com.skripsi.perpustakaanapp.core.responses.DetailBookResponse
import com.skripsi.perpustakaanapp.core.responses.GeneralResponse
import com.skripsi.perpustakaanapp.core.responses.StatusFavoriteResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailBookViewModel(private val repository: LibraryRepository) : ViewModel() {

    val resourceDeleteBook = MutableLiveData<Event<Resource<String?>>>()
    val resourceLoanBook = MutableLiveData<Event<Resource<String?>>>()
    val resourceDetailBook = MutableLiveData<Event<Resource<Book?>>>()
    val resourceChangeStatusFavorite = MutableLiveData<Event<Resource<String?>>>()
    val isFavorite = MutableLiveData<Event<Boolean?>>()


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

    fun getDetailBook(token: String, bookId: String?) {
        resourceDetailBook.postValue(Event(Resource.Loading()))
        val modelBookId = ModelBookId(bookId)
        val response = repository.getDetailBook(token, modelBookId)
        response.enqueue(object : Callback<DetailBookResponse> {
            override fun onResponse(
                call: Call<DetailBookResponse>,
                response: Response<DetailBookResponse>
            ) {
                if (response.body()?.code == 0) {
                    val book = Book(null, response.body()?.title, null, null,null,null,null,null,null, response.body()?.imageUrl)
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

    fun changeStatusFavorite(token: String, username: String?, bookId: String?, isFavorite: Boolean?) {
        resourceChangeStatusFavorite.postValue(Event(Resource.Loading()))
        val data = ModelForChangeStatusFavorite(username, bookId, isFavorite)
        val response = repository.changeStatusFavorite(token, data)
        response.enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(
                call: Call<GeneralResponse>,
                response: Response<GeneralResponse>
            ) {
                if (response.body()?.code == 0) {
                    resourceChangeStatusFavorite.postValue(Event(Resource.Success(response.body()?.message)))
                }
                else {
                    resourceChangeStatusFavorite.postValue(Event(Resource.Error(response.body()?.message)))
                }
            }

            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                resourceChangeStatusFavorite.postValue(Event(Resource.Error(t.message)))
            }
        })
    }

    fun statusFavorite(token: String, username: String?, bookId: String?) {
        val data = ModelForCreateTransaction(username, bookId)
        val response = repository.getStatusFavorite(token, data)
        response.enqueue(object : Callback<StatusFavoriteResponse> {
            override fun onResponse(
                call: Call<StatusFavoriteResponse>,
                response: Response<StatusFavoriteResponse>
            ) {
                if (response.body()?.code == 0) {
                    isFavorite.postValue(Event(response.body()?.isFavorite))
                }
            }

            override fun onFailure(call: Call<StatusFavoriteResponse>, t: Throwable) {
                Log.v("DetailBookViewModel", t.message.toString())
            }
        })
    }
}