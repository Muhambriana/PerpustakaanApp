package com.skripsi.perpustakaanapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.databinding.ActivityHomeAdminBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.ui.admin.bookmanagerial.createbook.CreateBookActivity
import com.skripsi.perpustakaanapp.ui.admin.listuser.UserActivity
import com.skripsi.perpustakaanapp.ui.admin.pendingloan.PendingLoanActivity
import com.skripsi.perpustakaanapp.ui.admin.usermanagerial.createnewadmin.CreateNewAdminActivity
import com.skripsi.perpustakaanapp.ui.admin.usermanagerial.scanattendance.ScannerActivity
import com.skripsi.perpustakaanapp.ui.book.listbook.BookActivity
import com.skripsi.perpustakaanapp.ui.login.LoginActivity

class HomeAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeAdminBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: HomeViewModel
    private var doubleBackPressed = false

    private val client = RetrofitClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firstInitialization()
        cardListener()
    }

    private fun firstInitialization() {
        if (intent.extras!=null){
            supportActionBar?.title = "Hi, ${intent.getStringExtra("FIRST_NAME")}"
        }

        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
            HomeViewModel::class.java
        )
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
        if (doubleBackPressed) {
            super.onBackPressed()
            return
        }
        doubleBackPressed = true
        Snackbar.make(binding.root, "Tekan Sekali Lagi Untuk Keluar", Snackbar.LENGTH_LONG)
            .show()
        Handler(Looper.getMainLooper()).postDelayed({ doubleBackPressed = false }, 2000)
    }

    private fun cardListener() {
        val clickListener = View.OnClickListener { view ->
            when (view.id){
                binding.cardCreateBook.id -> {
                    val intent = Intent(this, CreateBookActivity::class.java)
                    startActivity(intent)
                }
                binding.cardCreateAdmin.id -> {
                    val intent = Intent(this, CreateNewAdminActivity::class.java)
                    startActivity(intent)
                }
                binding.cardBookList.id -> {
                    val intent = Intent(this, BookActivity::class.java)
                    startActivity(intent)
                }
                binding.cardPendingLoanList.id -> {
                    val intent = Intent(this, PendingLoanActivity::class.java)
                    startActivity(intent)
                }
                binding.cardUserList.id -> {
                    val intent = Intent(this, UserActivity::class.java)
                    startActivity(intent)
                }
                binding.cardScanVisitors.id -> {
                    val intent = Intent(this, ScannerActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        binding.cardCreateBook.setOnClickListener(clickListener)
        binding.cardCreateAdmin.setOnClickListener(clickListener)
        binding.cardBookList.setOnClickListener(clickListener)
        binding.cardPendingLoanList.setOnClickListener(clickListener)
        binding.cardUserList.setOnClickListener(clickListener)
        binding.cardScanVisitors.setOnClickListener(clickListener)
    }

    private fun userLogout() {
        sessionManager = SessionManager(this)

        viewModel.userLogout(sessionManager.fetchAuthToken().toString())

        viewModel.resourceLogout.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
                when(resource) {
                    is MyResource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is MyResource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        startIntentBackToLogin()
                    }
                    is MyResource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        MyAlertDialog.show(this, R.drawable.icon_cancel, "ERROR", resource.message.toString())
                    }
                }
            }
        }
    }

    private fun startIntentBackToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}