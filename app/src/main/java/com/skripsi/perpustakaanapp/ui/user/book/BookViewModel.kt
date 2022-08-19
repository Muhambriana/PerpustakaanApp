package com.skripsi.perpustakaanapp.ui.user.book

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.models.Book
import com.skripsi.perpustakaanapp.core.models.BookList
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.Event
import com.skripsi.perpustakaanapp.core.resource.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookViewModel(private val repository: LibraryRepository) : ViewModel() {

    val resourceBook = MutableLiveData<Event<Resource<List<Book>>>>()

    fun getAllBooks(token: String) {
        resourceBook.postValue(Event(Resource.Loading()))
        val response = repository.getAllBooks(token)
        response.enqueue(object : Callback<BookList> {
            override fun onResponse(
                call: Call<BookList>,
                response: Response<BookList>)
            {
                if (response.body()?.code == 0) {
                    resourceBook.postValue(Event(Resource.Success(response.body()?.bookItems)))
                } else {
                    resourceBook.postValue(Event(Resource.Error(response.body()?.message)))
                }
            }

            override fun onFailure(
                call: Call<BookList>,
                t: Throwable)
            {
                resourceBook.postValue(Event(Resource.Error(t.message)))
            }
        })
    }
}