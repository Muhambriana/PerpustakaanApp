package com.skripsi.perpustakaanapp.ui.admin.bookmanagerial.updatebook

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

class UpdateBookViewModel(private val repository: LibraryRepository) : ViewModel(){

    val resourceUpdateBook = MutableLiveData<MyEvent<MyResource<String?>>>()
    val resourceUpdateImage = MutableLiveData<MyEvent<MyResource<String?>>>()
    val resourceUpdateEBook = MutableLiveData<MyEvent<MyResource<String?>>>()
    val resourceBookCategory = MutableLiveData<MyEvent<MyResource<List<BookCategory>>>>()

    fun updateBook(token: String,bookId: String, title: String, author: String, publisher: String, publisherDate: String, copies: String, description:String, category: String, imageUrl: String?, eBook: String?){
        resourceUpdateBook.postValue(MyEvent(MyResource.Loading()))
        val book = Book(bookId, title, author, publisher, publisherDate, copies, description, imageUrl, eBook, category)
        val post = repository.updateBook(token, book)
        post.enqueue(object : Callback<GeneralResponse>{
            override fun onResponse(
                call: Call<GeneralResponse>,
                response: Response<GeneralResponse>
            ) {
                if (response.body()?.code == 0) {
                    resourceUpdateBook.postValue(MyEvent(MyResource.Success(response.body()?.message)))
                }
                else {
                    resourceUpdateBook.postValue((MyEvent(MyResource.Error(response.body()?.message))))
                }
            }

            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                resourceUpdateBook.postValue(MyEvent(MyResource.Error("Connection Failed")))
//                t.message?.let { Log.e(" UpdateBookViewModel", it) }
            }
        })
    }

    fun updateBookImage(token: String, bookId: String, image: MultipartBody.Part?) {
        resourceUpdateImage.postValue(MyEvent(MyResource.Loading()))
        val id = RequestBody.create(MediaType.parse("text/plain"), bookId)
        val post = repository.updateImage(token, id, null, image)
        post.enqueue(object: Callback<GeneralResponse> {
            override fun onResponse(
                call: Call<GeneralResponse>,
                response: Response<GeneralResponse>
            ) {
                if (response.body()?.code == 0) {
                    resourceUpdateImage.postValue(MyEvent(MyResource.Success(response.body()?.message)))
                }
                else {
                    resourceUpdateImage.postValue(MyEvent(MyResource.Error(response.body()?.message)))
                }
            }

            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                resourceUpdateImage.postValue(MyEvent(MyResource.Error("Connection Failed")))
//                t.message?.let { Log.e(" UpdateBookViewModel", it) }
            }
        })
    }

    fun updateEBook(token: String, bookId: String, pdf: MultipartBody.Part?) {
        resourceUpdateEBook.postValue(MyEvent(MyResource.Loading()))
        val id = RequestBody.create(MediaType.parse("text/plain"), bookId)
        val post = repository.updateEBook(token, id, pdf)
        post.enqueue(object: Callback<GeneralResponse> {
            override fun onResponse(
                call: Call<GeneralResponse>,
                response: Response<GeneralResponse>
            ) {
                if (response.body()?.code == 0) {
                    resourceUpdateEBook.postValue(MyEvent(MyResource.Success(response.body()?.message)))
                }
                else {
                    resourceUpdateEBook.postValue(MyEvent(MyResource.Error(response.body()?.message)))
                }
            }

            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                resourceUpdateEBook.postValue(MyEvent(MyResource.Error("Connection Failed")))
//                t.message?.let { Log.e(" UpdateBookViewModel", it) }
            }
        })
    }

    fun getAllBookCategory(token: String){
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
                resourceBookCategory.postValue(MyEvent(MyResource.Error("Connection Failed")))
//                t.message?.let { Log.e(" UpdateBookViewModel", it) }
            }
        })
    }

}