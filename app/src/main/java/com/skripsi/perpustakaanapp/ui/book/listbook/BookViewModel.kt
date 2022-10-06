package com.skripsi.perpustakaanapp.ui.book.listbook

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.models.Book
import com.skripsi.perpustakaanapp.core.models.ModelForSearchBook
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyEvent
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.core.responses.ListBookResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookViewModel(private val repository: LibraryRepository) : ViewModel() {

    val resourceBook = MutableLiveData<MyEvent<MyResource<List<Book>>>>()
    val resourceSearchBook = MutableLiveData<MyEvent<MyResource<List<Book>>>>()

    fun getAllBooks(token: String) {
        resourceBook.postValue(MyEvent(MyResource.Loading()))
        val response = repository.getAllBooks(token)
        response.enqueue(object : Callback<ListBookResponse> {
            override fun onResponse(
                call: Call<ListBookResponse>,
                response: Response<ListBookResponse>)
            {
                if (response.body()?.code == 0) {
                    resourceBook.postValue(MyEvent(MyResource.Success(response.body()?.bookItems)))
                } else {
                    resourceBook.postValue(MyEvent(MyResource.Error(response.body()?.message)))
                }
            }

            override fun onFailure(call: Call<ListBookResponse>, t: Throwable) {
                resourceBook.postValue(MyEvent(MyResource.Error(t.message)))
            }
        })
    }

    fun searchBook(token: String, title: String?) {
        resourceSearchBook.postValue(MyEvent(MyResource.Loading()))
        val model = ModelForSearchBook(title)
        val response = repository.searchBook(token, model)
        response.enqueue(object : Callback<ListBookResponse> {
            override fun onResponse(
                call: Call<ListBookResponse>,
                response: Response<ListBookResponse>
            ) {
                if (response.body()?.code == 0) {
                    if (response.body()?.bookItems?.isEmpty() == false) {
                    resourceSearchBook.postValue(MyEvent(MyResource.Success(response.body()?.bookItems)))
                    } else {
                    resourceSearchBook.postValue(MyEvent((MyResource.Error(response.body()?.message, response.body()?.bookItems))))
                    }
                } else {
                    resourceSearchBook.postValue(MyEvent(MyResource.Error(response.body()?.message)))
                }
            }

            override fun onFailure(call: Call<ListBookResponse>, t: Throwable) {
                resourceSearchBook.postValue(MyEvent(MyResource.Error(t.message)))
            }
        })
    }
}