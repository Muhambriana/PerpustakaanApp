package com.skripsi.perpustakaanapp.ui.admin.bookmanagerial.createbook

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.skripsi.perpustakaanapp.core.models.Book
import com.skripsi.perpustakaanapp.core.models.BookCategory
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyEvent
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.core.responses.GeneralResponse
import com.skripsi.perpustakaanapp.core.responses.ListCategoryResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateBookViewModel(private val repository: LibraryRepository) : ViewModel() {

    val resourceCreateBook = MutableLiveData<MyEvent<MyResource<String?>>>()
    val resourceBookCategory = MutableLiveData<MyEvent<MyResource<List<BookCategory>>>>()

    fun createBook(token: String, title: String, author: String, publisher: String, publisherDate: String, stock: String, description:String, category: String?, image: MultipartBody.Part?, pdf: MultipartBody.Part?){
        resourceCreateBook.postValue(MyEvent(MyResource.Loading()))
        val book = Book(null, title, author, publisher, publisherDate, stock, description, null, null, category)
        val jsonString = Gson().toJson(book)
        val data = RequestBody.create(MediaType.parse("text/plain"), jsonString)
        println("kocak isi pdf: $pdf")
        val post = repository.createBook(token, data, image, pdf)
        post.enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(
                call: Call<GeneralResponse>,
                response: Response<GeneralResponse>
            ) {
                if (response.body()?.code == 0) {
                    resourceCreateBook.postValue(MyEvent(MyResource.Success(response.body()?.message)))
                }
                else {
                    resourceCreateBook.postValue(MyEvent(MyResource.Error(response.body()?.message)))
                }
            }
            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                resourceCreateBook.postValue(MyEvent(MyResource.Error(t.message)))
            }
        })
    }

    fun getAllBookCategory(token: String){
        resourceCreateBook.postValue(MyEvent(MyResource.Loading()))
        val post = repository.getAllCategory(token)
        post.enqueue(object : Callback<ListCategoryResponse> {
            override fun onResponse(
                call: Call<ListCategoryResponse>,
                response: Response<ListCategoryResponse>
            ) {
                if (response.body()?.code == 0) {
                    if (response.body()?.categoryItems?.isEmpty() == true) {
                        resourceBookCategory.postValue(MyEvent(MyResource.Empty()))
                    }

                    resourceBookCategory.postValue(MyEvent(MyResource.Success(response.body()?.categoryItems)))
                }
                else {
                    resourceBookCategory.postValue(MyEvent(MyResource.Error(response.body()?.message)))
                }
            }
            override fun onFailure(call: Call<ListCategoryResponse>, t: Throwable) {
                resourceBookCategory.postValue(MyEvent(MyResource.Error(t.message)))
            }
        })
    }

}