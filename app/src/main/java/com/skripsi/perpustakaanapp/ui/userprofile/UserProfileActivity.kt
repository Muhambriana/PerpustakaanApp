package com.skripsi.perpustakaanapp.ui.userprofile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.models.User
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.Resource
import com.skripsi.perpustakaanapp.databinding.ActivityUserProfileBinding

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: UserProfileViewModel

    private var detailUser: User? = null
    private val client = RetrofitClient
    private val context = this@UserProfileActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //when still loading the data, action bar will show nothing
        supportActionBar?.title = ""

        sessionManager = SessionManager(this)

        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
            UserProfileViewModel::class.java
        )

        if (intent.extras != null){
            if (intent.getStringExtra(OFFICER_USERNAME) != null) {
                setDetailUser(intent.getStringExtra(OFFICER_USERNAME).toString())
            }
            else {
                showDetailUser()
            }
        }
        
    }

    override fun onNavigateUp(): Boolean {
        onBackPressed()
        return super.onNavigateUp()
    }

    private fun setDetailUser(officerUsername: String) {
        viewModel.getDetailUser(sessionManager.fetchAuthToken().toString(), officerUsername)
        
        viewModel.resourceDetailUser.observe(this) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        //progress bar
                    }
                    is Resource.Success -> {
                        detailUser = resource.data
                        showDetailUser()
                    }
                }
            }
        }
    }

    private fun showDetailUser() {
        supportActionBar?.title = detailUser?.firstName
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
        const val OFFICER_USERNAME = "officer_username"
    }
}