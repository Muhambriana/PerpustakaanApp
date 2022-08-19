package com.skripsi.perpustakaanapp.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.databinding.ActivityHomeAdminBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.ui.admin.createbook.CreateBookActivity
import com.skripsi.perpustakaanapp.ui.admin.createnewadmin.CreateNewAdminActivity
import com.skripsi.perpustakaanapp.ui.login.LoginActivity
import com.skripsi.perpustakaanapp.ui.user.book.BookActivity

class HomeAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeAdminBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: HomeViewModel

    private val client = RetrofitClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.extras!=null){
            supportActionBar?.title = "Hi, ${intent.getStringExtra("first_name")}"
        }

        viewModel = ViewModelProvider(this, MViewModelFactory(LibraryRepository(client))).get(
            HomeViewModel::class.java
        )

        cardListener()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout_menu -> {
                userLogout()
                true
            }
            else -> true
        }
    }

    override fun onBackPressed() {
        onPause()
        super.onBackPressed()
    }

    private fun userLogout() {
        sessionManager = SessionManager(this)
        viewModel.userLogout(token = "Bearer ${sessionManager.fetchAuthToken()}")
        viewModel.isSuccess.observe(this) {
            viewModel.isSuccess.postValue(null)
            if (it == true) {
                val intent = Intent(this@HomeAdminActivity, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
        viewModel.errorMessage.observe(this) {
            if (it != null) {
                viewModel.errorMessage.value = null
                MyAlertDialog.showAlertDialog(this@HomeAdminActivity, R.drawable.icon_cancel, "ERROR", it)
            }
        }
    }

    private fun cardListener() {
        val clickListener = View.OnClickListener { view ->
            when (view.id){
                R.id.create_book -> {
                    val intent = Intent(this@HomeAdminActivity, CreateBookActivity::class.java)
                    startActivity(intent)
                }
                R.id.create_admin -> {
                    val intent = Intent(this@HomeAdminActivity, CreateNewAdminActivity::class.java)
                    startActivity(intent)
                }
                R.id.book_list -> {
                    val intent = Intent(this@HomeAdminActivity, BookActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        binding.createBook.setOnClickListener(clickListener)
        binding.createAdmin.setOnClickListener(clickListener)
        binding.bookList.setOnClickListener(clickListener)
    }
}