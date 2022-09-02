package com.skripsi.perpustakaanapp.ui.userprofile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.skripsi.perpustakaanapp.R
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
    private var username: String? = null
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

        username = intent.getStringExtra(USERNAME)
        if (intent.extras != null){
            if (username != null) {
                setDetailUser()
            }
            else {
                showDetailUser()
            }
        }
        
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (sessionManager.fetchUsername() == username){
            menuInflater.inflate(R.menu.activity_book_menu, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> true
        }
    }

    private fun setDetailUser() {
        username?.let { viewModel.getDetailUser(sessionManager.fetchAuthToken().toString(), it) }
        
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
        const val USERNAME = "username"
    }
}