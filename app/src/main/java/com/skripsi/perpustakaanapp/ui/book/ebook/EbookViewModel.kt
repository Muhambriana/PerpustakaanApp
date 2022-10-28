package com.skripsi.perpustakaanapp.ui.book.ebook

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyEvent
import com.skripsi.perpustakaanapp.core.resource.MyResource
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayInputStream
import java.io.InputStream


class EbookViewModel(private val repository: LibraryRepository): ViewModel() {

    val resourcePDF = MutableLiveData<MyEvent<MyResource<InputStream>>>()

    fun showPDF(token:String, fileName: String) {
        val post = repository.showPDF(token, fileName)
        post.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                resourcePDF.postValue(MyEvent(MyResource.Success(response.body()?.byteStream())))
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                resourcePDF.postValue(MyEvent(MyResource.Error(t.message)))
            }
        })
    }

}