package com.skripsi.perpustakaanapp.ui.admin.bookmanagerial.createbook

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.skripsi.perpustakaanapp.core.models.Book
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyEvent
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.core.responses.GeneralResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateBookViewModel(private val repository: LibraryRepository) : ViewModel() {

    val resourceUpdateBook = MutableLiveData<MyEvent<MyResource<String?>>>()

    fun createBook(token: String, title: String, edition: String, author: String, publisher: String, publisherDate: String, copies: String, source: String, remark: String, image: MultipartBody.Part?){
        resourceUpdateBook.postValue(MyEvent(MyResource.Loading()))
        val book = Book(null, title, edition, author, publisher, publisherDate, copies, source, remark, null)
        val jsonString = Gson().toJson(book)
        val data = RequestBody.create(MediaType.parse("text/plain"), jsonString)
        val post = repository.createBook(token, data, image)
        post.enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(
                call: Call<GeneralResponse>,
                response: Response<GeneralResponse>
            ) {
                if (response.body()?.code == 0) {
                    resourceUpdateBook.postValue(MyEvent(MyResource.Success(response.body()?.message)))
                }
                else {
                    resourceUpdateBook.postValue(MyEvent(MyResource.Error(response.body()?.message)))
                }
            }
            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                resourceUpdateBook.postValue(MyEvent(MyResource.Error(t.message)))
            }
        })
    }

}