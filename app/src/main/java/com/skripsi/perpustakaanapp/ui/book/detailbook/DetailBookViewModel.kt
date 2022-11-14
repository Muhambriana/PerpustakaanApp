package com.skripsi.perpustakaanapp.ui.book.detailbook

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.models.Book
import com.skripsi.perpustakaanapp.core.models.ModelBookId
import com.skripsi.perpustakaanapp.core.models.ModelForChangeStatusFavorite
import com.skripsi.perpustakaanapp.core.models.ModelForCreateTransaction
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyEvent
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.core.responses.DetailBookResponse
import com.skripsi.perpustakaanapp.core.responses.GeneralResponse
import com.skripsi.perpustakaanapp.core.responses.StatusFavoriteResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailBookViewModel(private val repository: LibraryRepository) : ViewModel() {

    val resourceDeleteBook = MutableLiveData<MyEvent<MyResource<String?>>>()
    val resourceLoanBook = MutableLiveData<MyEvent<MyResource<String?>>>()
    val resourceDetailBook = MutableLiveData<MyEvent<MyResource<Book?>>>()
    val resourceChangeStatusFavorite = MutableLiveData<MyEvent<MyResource<String?>>>()
    val isFavorite = MutableLiveData<MyEvent<Boolean?>>()


    fun deleteBook(token: String, bookId: String) {
        resourceDeleteBook.postValue(MyEvent(MyResource.Loading()))
        val modelBookId = ModelBookId(bookId)
        val post = repository.deleteBook(token, modelBookId)
        post.enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(
                call: Call<GeneralResponse>,
                response: Response<GeneralResponse>,
            ) {
                if (response.body()?.code == 0) {
                    resourceDeleteBook.postValue(MyEvent(MyResource.Success(response.body()?.message)))
                } else {
                    resourceDeleteBook.postValue(MyEvent(MyResource.Error(response.body()?.message)))
                }
            }

            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                resourceDeleteBook.postValue(MyEvent(MyResource.Error(t.message)))
            }
        })
    }

    fun createTransaction(token: String, username: String?, bookId: String?) {
        resourceLoanBook.postValue(MyEvent(MyResource.Loading()))
        val modelForCreateTransaction = ModelForCreateTransaction(username, bookId)
        val post = repository.createTransaction(token, modelForCreateTransaction)
        post.enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(
                call: Call<GeneralResponse>,
                response: Response<GeneralResponse>,
            ) {
                if (response.body()?.code == 0) {
                    resourceLoanBook.postValue(MyEvent(MyResource.Success(response.body()?.message)))
                } else {
                    resourceLoanBook.postValue(MyEvent(MyResource.Error(response.body()?.message)))
                }
            }

            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                resourceLoanBook.postValue(MyEvent(MyResource.Error(t.message)))
            }
        })
    }

    fun getDetailBook(token: String, bookId: String?) {
        resourceDetailBook.postValue(MyEvent(MyResource.Loading()))
        val modelBookId = ModelBookId(bookId)
        val response = repository.getDetailBook(token, modelBookId)
        response.enqueue(object : Callback<DetailBookResponse> {
            override fun onResponse(
                call: Call<DetailBookResponse>,
                response: Response<DetailBookResponse>
            ) {
                if (response.body()?.code == 0) {
                    val book = Book(
                        response.body()?.bookId,
                        response.body()?.title,
                        response.body()?.author,
                        response.body()?.publisher,
                        response.body()?.publisherDate,
                        response.body()?.stock,
                        response.body()?.description,
                        response.body()?.imageUrl,
                        response.body()?.eBook,
                        response.body()?.category
                    )
                    resourceDetailBook.postValue(MyEvent(MyResource.Success(book)))
                }
                else {
                    resourceDetailBook.postValue((MyEvent(MyResource.Error(response.body()?.message))))
                }
            }

            override fun onFailure( call: Call<DetailBookResponse>, t: Throwable) {
                resourceDetailBook.postValue(MyEvent(MyResource.Error(t.message)))
            }
        })
    }

    fun changeStatusFavorite(token: String, username: String?, bookId: String?, isFavorite: Boolean?) {
        resourceChangeStatusFavorite.postValue(MyEvent(MyResource.Loading()))
        val data = ModelForChangeStatusFavorite(username, bookId, isFavorite)
        val response = repository.changeStatusFavorite(token, data)
        response.enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(
                call: Call<GeneralResponse>,
                response: Response<GeneralResponse>
            ) {
                if (response.body()?.code == 0) {
                    resourceChangeStatusFavorite.postValue(MyEvent(MyResource.Success(response.body()?.message)))
                }
                else {
                    resourceChangeStatusFavorite.postValue(MyEvent(MyResource.Error(response.body()?.message)))
                }
            }

            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                resourceChangeStatusFavorite.postValue(MyEvent(MyResource.Error(t.message)))
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
                    isFavorite.postValue(MyEvent(response.body()?.isFavorite))
                }
            }

            override fun onFailure(call: Call<StatusFavoriteResponse>, t: Throwable) {
                Log.v("DetailBookViewModel", t.message.toString())
            }
        })
    }
}