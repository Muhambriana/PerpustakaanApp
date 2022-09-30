package com.skripsi.perpustakaanapp.ui.admin.usermanagerial.createnewadmin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.models.User
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.Event
import com.skripsi.perpustakaanapp.core.resource.Resource
import com.skripsi.perpustakaanapp.core.responses.GeneralResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateNewAdminViewModel(private val repository: LibraryRepository) : ViewModel() {

    val resourceCreateAdmin = MutableLiveData<Event<Resource<String?>>>()

    fun createNewAdmin(username: String, password: String, firstName: String, lastName: String, roleName: String, email: String, phoneNo: String, address: String, gender: Int){
        resourceCreateAdmin.postValue(Event(Resource.Loading()))
        val user = User(username, password, firstName, lastName, roleName, email, phoneNo, address, gender)
        val post = repository.createUser(user)
        post.enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>
            ) {
                if(response.body()?.code == 0) {
                    resourceCreateAdmin.postValue(Event(Resource.Success(response.body()?.message)))
                } else {
                    resourceCreateAdmin.postValue(Event(Resource.Error(response.body()?.message)))
                }
            }

            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                resourceCreateAdmin.postValue(Event(Resource.Error(t.message)))
            }
        })
    }

}