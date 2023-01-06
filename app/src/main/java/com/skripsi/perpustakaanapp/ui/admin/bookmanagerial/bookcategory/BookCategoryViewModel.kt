package com.skripsi.perpustakaanapp.ui.admin.bookmanagerial.bookcategory

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.models.BookCategory
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyEvent
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.core.responses.GeneralResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookCategoryViewModel(private val repository: LibraryRepository) : ViewModel() {
    
    val resourceCreateCategory = MutableLiveData<MyEvent<MyResource<String?>>>()

    fun createCategory(token: String, categoryName: String){
       resourceCreateCategory.postValue(MyEvent(MyResource.Loading()))
        val data = BookCategory(null, categoryName)
        val post = repository.createBookCategory(token, data)
        post.enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(
                call: Call<GeneralResponse>,
                response: Response<GeneralResponse>
            ) {
                if (response.body()?.code == 0) {
                    resourceCreateCategory.postValue(MyEvent(MyResource.Success(response.body()?.message)))
                }
                else {
                    resourceCreateCategory.postValue(MyEvent(MyResource.Error(response.body()?.message)))
                }
            }
            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                resourceCreateCategory.postValue(MyEvent(MyResource.Error("Failed Connection, Check Your Connection")))
//                t.message?.let { Log.e("BookCategoryViewModel", it) }
            }
        })
    }
}