package com.skripsi.perpustakaanapp.ui.member.favoritebook

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.models.Book
import com.skripsi.perpustakaanapp.core.models.FavoriteBook
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.Event
import com.skripsi.perpustakaanapp.core.resource.Resource
import com.skripsi.perpustakaanapp.core.responses.ListFavoriteResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoriteBookViewModel(private val repository: LibraryRepository) : ViewModel() {
    val resourceFavorite = MutableLiveData<Event<Resource<List<Book>>>>()

    fun getAllFavorites(token:String){
        resourceFavorite.postValue(Event(Resource.Loading()))
        val response = repository.getAllFavorite(token)
        response.enqueue(object : Callback<ListFavoriteResponse> {
            override fun onResponse(
                call: Call<ListFavoriteResponse>,
                response: Response<ListFavoriteResponse>
            ) {
                if (response.body()?.code == 0) {
                    resourceFavorite.postValue(Event(Resource.Success(response.body()?.favoriteItems)))
                } else {
                    resourceFavorite.postValue(Event(Resource.Error(response.body()?.message)))
                }
            }
            override fun onFailure(call: Call<ListFavoriteResponse>, t: Throwable) {
                resourceFavorite.postValue(Event(Resource.Error(t.message)))
            }
        })
    }
}