package com.skripsi.perpustakaanapp.ui.userprofile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.models.ModelUsername
import com.skripsi.perpustakaanapp.core.models.User
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyEvent
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.core.responses.DetailUserResponse
import com.skripsi.perpustakaanapp.core.responses.GeneralResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserProfileViewModel(private val repository: LibraryRepository): ViewModel() {

    val resourceDetailUser = MutableLiveData<MyEvent<MyResource<User?>>>()
    val resourceDeleteMember = MutableLiveData<MyEvent<MyResource<String?>>>()
    val resourceUpdateImage = MutableLiveData<MyEvent<MyResource<String?>>>()

    fun getDetailUser(token: String, username: String) {
        resourceDetailUser.postValue(MyEvent(MyResource.Loading()))
        val modelUsername = ModelUsername(username)
        val response = repository.getDetailUser(token, modelUsername)
        response.enqueue(object: Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                if (response.body()?.code == 0) {
                    val user = User(response.body()?.username, null, response.body()?.firstName, response.body()?.lastName, response.body()?.roleName, response.body()?.email, response.body()?.phoneNo, response.body()?.address, response.body()?.gender, response.body()?.avatar, response.body()?.educationLevel)
                    resourceDetailUser.postValue(MyEvent(MyResource.Success(user)))
                }
                else {
                    resourceDetailUser.postValue(MyEvent(MyResource.Error(response.body()?.message)))
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                resourceDetailUser.postValue(MyEvent(MyResource.Error(t.message)))
            }
        })
    }

    fun deleteMember(token: String, username: String) {
        resourceDeleteMember.postValue(MyEvent(MyResource.Loading()))
        val modelUsername = ModelUsername(username)
        val response = repository.deleteMember(token, modelUsername)
        response.enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(
                call: Call<GeneralResponse>,
                response: Response<GeneralResponse>
            ) {
                if (response.body()?.code == 0) {
                    resourceDeleteMember.postValue(MyEvent(MyResource.Success(response.body()?.message)))
                } else {
                    resourceDeleteMember.postValue(MyEvent(MyResource.Error(response.body()?.message)))
                }
            }
            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                resourceDeleteMember.postValue(MyEvent(MyResource.Error(t.message)))
            }
        })
    }

    fun updateImage(token: String, username: String, image: MultipartBody.Part?) {
        resourceUpdateImage.postValue(MyEvent(MyResource.Loading()))
        val id = RequestBody.create(MediaType.parse("text/plain"), username)
        println("kocak id: $id")
        val post = repository.updateImage(token, null, id, image)
        post.enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(
                call: Call<GeneralResponse>,
                response: Response<GeneralResponse>
            ) {
                if(response.body()?.code == 0) {
                    resourceUpdateImage.postValue(MyEvent(MyResource.Success(response.body()?.message)))
                } else {
                    resourceUpdateImage.postValue(MyEvent(MyResource.Error(response.body()?.message)))
                }
            }

            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                resourceUpdateImage.postValue(MyEvent(MyResource.Error(t.message)))
            }
        })
    }
}