package com.skripsi.perpustakaanapp.ui.admin.listuser

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.adapter.UserAdapter
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.models.User
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.databinding.ActivityUserBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.ui.userprofile.UserProfileActivity

class UserActivity : AppCompatActivity() {

    private lateinit var binding : ActivityUserBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: UserViewModel

    private val client = RetrofitClient
    private val userAdapter = UserAdapter()
    private var userData : List<User>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firstInitialization()
        getUserData()
    }

    override fun supportNavigateUpTo(upIntent: Intent) {

        onBackPressed()
        super.supportNavigateUpTo(upIntent)
    }

    private fun firstInitialization() {
        supportActionBar?.title = "Anggota Perpustakaan"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sessionManager = SessionManager(this)

        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
            UserViewModel::class.java
        )
    }

    private fun getUserData() {
        viewModel.getAllMember(sessionManager.fetchAuthToken().toString())

        viewModel.resourceMember.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
                when(resource) {
                    is MyResource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is MyResource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        userData = resource.data
                        showRecycleList()
                    }
                    is MyResource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        MyAlertDialog.showWith2Event(this, R.drawable.icon_cancel, resource.message.toString(), resources.getString(R.string.refresh), resources.getString(R.string.back_to_dashboard),
                            { _, _ ->
                                getUserData()
                            },
                            { _,_ ->
                                finish()
                            }
                        )
                    }
                }
            }
        }

    }

    private fun showRecycleList() {
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.adapter = userAdapter
        userAdapter.setUserList(userData)

        // On user item click
        userItemClick()
    }

    private fun userItemClick() {
        userAdapter.onItemClick = {
            val intent = Intent(this, UserProfileActivity::class.java)
            intent.putExtra(UserProfileActivity.EXTRA_DATA, it)
            startActivity(intent)
        }
    }
}