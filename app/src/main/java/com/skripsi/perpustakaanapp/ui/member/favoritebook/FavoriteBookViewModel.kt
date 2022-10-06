package com.skripsi.perpustakaanapp.ui.member.favoritebook

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.models.Book
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyEvent
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.core.responses.ListFavoriteResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoriteBookViewModel(private val repository: LibraryRepository) : ViewModel() {
    val resourceFavorite = MutableLiveData<MyEvent<MyResource<List<Book>>>>()

    fun getAllFavorites(token:String){
        resourceFavorite.postValue(MyEvent(MyResource.Loading()))
        val response = repository.getAllFavorite(token)
        response.enqueue(object : Callback<ListFavoriteResponse> {
            override fun onResponse(
                call: Call<ListFavoriteResponse>,
                response: Response<ListFavoriteResponse>
            ) {
                if (response.body()?.code == 0) {
                    resourceFavorite.postValue(MyEvent(MyResource.Success(response.body()?.favoriteItems)))
                } else {
                    resourceFavorite.postValue(MyEvent(MyResource.Error(response.body()?.message)))
                }
            }
            override fun onFailure(call: Call<ListFavoriteResponse>, t: Throwable) {
                resourceFavorite.postValue(MyEvent(MyResource.Error(t.message)))
            }
        })
    }
}