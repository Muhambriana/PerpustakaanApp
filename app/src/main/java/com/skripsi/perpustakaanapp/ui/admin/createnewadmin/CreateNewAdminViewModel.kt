package com.skripsi.perpustakaanapp.ui.admin.createnewadmin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skripsi.perpustakaanapp.core.models.User
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.responses.GeneralResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateNewAdminViewModel(private val repository: LibraryRepository) : ViewModel() {

    val isLoading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()

    var failMessage = MutableLiveData<String?>()

    fun createNewAdmin(userId: String, password: String, fullName: String, roleName: String, email: String, phoneNo: String, address: String, gender: Int){
        isLoading.value = true
        val user = User(userId, password, fullName, roleName, email, phoneNo, address, gender)
        val post = repository.createUser(user)
        post.enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>
            ) {
                isLoading.value = false
                if (response.body()?.code == 0 ) {
                    failMessage.value = ""
                }
                else {
                    failMessage.value = response.body()?.message
                }
            }

            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                isLoading.value = false
                errorMessage.postValue(t.message)
            }
        })
    }

}