package com.skripsi.perpustakaanapp.ui.userprofile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.models.ModelUsername
import com.skripsi.perpustakaanapp.core.models.User
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.Event
import com.skripsi.perpustakaanapp.core.resource.Resource
import com.skripsi.perpustakaanapp.core.responses.DetailUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserProfileViewModel(private val repository: LibraryRepository): ViewModel() {

    val resourceDetailUser = MutableLiveData<Event<Resource<User>>>()

    fun getDetailUser(token: String, officerUsername: String) {
        resourceDetailUser.postValue(Event(Resource.Loading()))
        val modelUsername = ModelUsername(officerUsername)
        val response = repository.getDetaiUser(token, modelUsername)
        response.enqueue(object: Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                if (response.body()?.code == 0) {
                    val user = User(null, null, response.body()?.firstName)
                    resourceDetailUser.postValue(Event(Resource.Success(user)))
                }
                else {
                    resourceDetailUser.postValue(Event(Resource.Error(response.body()?.message)))
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                resourceDetailUser.postValue(Event(Resource.Error(t.message)))
            }
        })
    }
}