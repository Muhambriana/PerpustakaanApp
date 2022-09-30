package com.skripsi.perpustakaanapp.ui.book.listbook

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.models.Book
import com.skripsi.perpustakaanapp.core.models.ModelForSearchBook
import com.skripsi.perpustakaanapp.core.responses.ListBookResponse
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.Event
import com.skripsi.perpustakaanapp.core.resource.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookViewModel(private val repository: LibraryRepository) : ViewModel() {

    val resourceBook = MutableLiveData<Event<Resource<List<Book>>>>()
    val resourceSearchBook = MutableLiveData<Event<Resource<List<Book>>>>()

    fun getAllBooks(token: String) {
        resourceBook.postValue(Event(Resource.Loading()))
        val response = repository.getAllBooks(token)
        response.enqueue(object : Callback<ListBookResponse> {
            override fun onResponse(
                call: Call<ListBookResponse>,
                response: Response<ListBookResponse>)
            {
                if (response.body()?.code == 0) {
                    resourceBook.postValue(Event(Resource.Success(response.body()?.bookItems)))
                } else {
                    resourceBook.postValue(Event(Resource.Error(response.body()?.message)))
                }
            }

            override fun onFailure(call: Call<ListBookResponse>, t: Throwable) {
                resourceBook.postValue(Event(Resource.Error(t.message)))
            }
        })
    }

    fun searchBook(token: String, title: String?) {
        resourceSearchBook.postValue(Event(Resource.Loading()))
        val model = ModelForSearchBook(title)
        val response = repository.searchBook(token, model)
        response.enqueue(object : Callback<ListBookResponse> {
            override fun onResponse(
                call: Call<ListBookResponse>,
                response: Response<ListBookResponse>
            ) {
                if (response.body()?.code == 0) {
                    resourceSearchBook.postValue(Event(Resource.Success(response.body()?.bookItems)))
                } else {
                    resourceBook.postValue(Event(Resource.Error(response.body()?.message)))
                }
            }

            override fun onFailure(call: Call<ListBookResponse>, t: Throwable) {
                resourceSearchBook.postValue(Event(Resource.Error(t.message)))
            }
        })
    }
}