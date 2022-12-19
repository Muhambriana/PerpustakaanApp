package com.skripsi.perpustakaanapp.ui.admin.listuser

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.adapter.UserAdapter
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.models.Book
import com.skripsi.perpustakaanapp.core.models.User
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.databinding.ActivityUserBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.ui.MySnackBar
import com.skripsi.perpustakaanapp.ui.member.favoritebook.FavoriteBookActivity
import com.skripsi.perpustakaanapp.ui.userprofile.UserProfileActivity

class UserActivity : AppCompatActivity() {

    private lateinit var binding : ActivityUserBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: UserViewModel
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private val client = RetrofitClient
    private val userAdapter = UserAdapter()
    private var userData : List<User>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firstInitialization()
        setLauncher()
        getUserData()
    }

    private fun firstInitialization() {
        supportActionBar?.title = "Anggota Perpustakaan"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sessionManager = SessionManager(this)

        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
            UserViewModel::class.java
        )
    }

    private fun setLauncher() {
        //For get return data after launch activity
        resultLauncher =  registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){ result ->
            if (result.resultCode == RESULT_OK) {
                //Re-run getUserData and update with the latest
                getUserData()
            } else if (result.resultCode == 202) {
                //Re-run getUserData and update with the latest
                getUserData()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_user_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (sessionManager.fetchUserRole() == "student") {
            val searchMenu = menu?.findItem(R.id.search_menu)
            searchMenu?.isVisible = false
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search_menu -> {
                searchViewListener(item)
                true
            }
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> true
        }
    }

    private fun searchViewListener(item: MenuItem) {
        // getting search view of our item.
        val searchView: SearchView = item.actionView as SearchView

        // below line is to call set on query text listener method.
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(title: String?): Boolean {
                searchMember(title)
                return false
            }

            override fun onQueryTextChange(msg: String): Boolean {
                return false
            }
        })

        item.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                hideRecycleList()
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                showRecycleList(null)
                return true
            }
        })
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
                        showRecycleList(null)
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

    private fun hideRecycleList() {
        binding.rvUser.layoutManager = null
        binding.rvUser.adapter = null
    }

    private fun showRecycleList(dataSearch: List<User>?) {
        binding.rvUser.layoutManager = GridLayoutManager(this, 2)
        binding.rvUser.adapter = userAdapter
        if (dataSearch == null) {
            userAdapter.setUserList(userData)
        } else {
            userAdapter.setUserList(dataSearch)
        }

        // On user item click
        userItemClick()
    }

    private fun userItemClick() {
        userAdapter.onItemClick = {
            val intent = Intent(this, UserProfileActivity::class.java)
            intent.putExtra(UserProfileActivity.EXTRA_DATA, it)
            resultLauncher.launch(intent)
        }
    }

    private fun searchMember(username: String?) {
        viewModel.searchMember(sessionManager.fetchAuthToken().toString(), username)

        viewModel.resourceSearchMember.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
                when(resource) {
                    is MyResource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is MyResource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        showRecycleList(resource.data)
                    }
                    is MyResource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        MySnackBar.showRed(binding.root, resource.message.toString())
                        showRecycleList(resource.data)
                    }
                    is MyResource.Empty -> {
                        binding.progressBar.visibility = View.GONE
                        MySnackBar.showBlack(binding.root, "User Tidak Ditemukan")
//                        binding.viewEmpty.root.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}