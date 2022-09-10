package com.skripsi.perpustakaanapp.ui.admin.bookmanagerial.updatebook

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.models.Book
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.Event
import com.skripsi.perpustakaanapp.core.resource.Resource
import com.skripsi.perpustakaanapp.core.responses.GeneralResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Multipart

class UpdateBookViewModel(private val repository: LibraryRepository) : ViewModel(){

    var resourceUpdateBook = MutableLiveData<Event<Resource<String?>>>()
    val resourceUpdateImage = MutableLiveData<Event<Resource<String?>>>()

    fun updateBook(token: String,bookId: String, title: String, edition: String, author: String, publisher: String, publisherDate: String, copies: String, source: String, remark: String){
        resourceUpdateBook.postValue(Event(Resource.Loading()))
        val book = Book(bookId, title, edition, author, publisher, publisherDate, copies, source, remark)
        val post = repository.updateBook(token, book)
        post.enqueue(object : Callback<GeneralResponse>{
            override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                if (response.body()?.code == 0) {
                    resourceUpdateBook.postValue(Event(Resource.Success(response.body()?.message)))
                }
                else {
                    resourceUpdateBook.postValue((Event(Resource.Error(response.body()?.message))))
                }
            }

            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                resourceUpdateBook.postValue(Event(Resource.Error(t.message)))
            }
        })
    }

    fun updateBookImage(token: String, bookId: RequestBody?, image: MultipartBody.Part?) {
        resourceUpdateImage.postValue(Event(Resource.Loading()))

    }

}