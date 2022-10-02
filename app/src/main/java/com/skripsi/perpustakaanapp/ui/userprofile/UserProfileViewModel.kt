package com.skripsi.perpustakaanapp.ui.userprofile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.models.ModelUsername
import com.skripsi.perpustakaanapp.core.models.User
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.Event
import com.skripsi.perpustakaanapp.core.resource.Resource
import com.skripsi.perpustakaanapp.core.responses.DetailUserResponse
import com.skripsi.perpustakaanapp.core.responses.GeneralResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserProfileViewModel(private val repository: LibraryRepository): ViewModel() {

    val resourceDetailUser = MutableLiveData<Event<Resource<User?>>>()
    val resourceDeleteMember = MutableLiveData<Event<Resource<String?>>>()
    val resourceUpdateImage = MutableLiveData<Event<Resource<String?>>>()

    fun getDetailUser(token: String, username: String) {
        resourceDetailUser.postValue(Event(Resource.Loading()))
        val modelUsername = ModelUsername(username)
        val response = repository.getDetailUser(token, modelUsername)
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

    fun deleteMember(token: String, username: String) {
        resourceDeleteMember.postValue(Event(Resource.Loading()))
        val modelUsername = ModelUsername(username)
        val response = repository.deleteMember(token, modelUsername)
        response.enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(
                call: Call<GeneralResponse>,
                response: Response<GeneralResponse>
            ) {
                if (response.body()?.code == 0) {
                    resourceDeleteMember.postValue(Event(Resource.Success(response.body()?.message)))
                } else {
                    resourceDeleteMember.postValue(Event(Resource.Error(response.body()?.message)))
                }
            }
            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                resourceDeleteMember.postValue(Event(Resource.Error(t.message)))
            }
        })
    }

    fun updateImage(token: String, username: String, image: MultipartBody.Part?) {
        resourceUpdateImage.postValue(Event(Resource.Loading()))
        val id = RequestBody.create(MediaType.parse("text/plain"), username)
        val post = repository.updateImage(token, null, id, image)
        post.enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(
                call: Call<GeneralResponse>,
                response: Response<GeneralResponse>
            ) {
                if(response.body()?.code == 0) {
                    resourceUpdateImage.postValue(Event(Resource.Success(response.body()?.message)))
                } else {
                    resourceUpdateImage.postValue(Event(Resource.Error(response.body()?.message)))
                }
            }

            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                resourceUpdateImage.postValue(Event(Resource.Error(t.message)))
            }
        })
    }
}