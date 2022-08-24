package com.skripsi.perpustakaanapp.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.databinding.ActivityHomeAdminBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.ui.admin.bookmanagerial.createbook.CreateBookActivity
import com.skripsi.perpustakaanapp.ui.admin.createnewadmin.CreateNewAdminActivity
import com.skripsi.perpustakaanapp.ui.admin.loanmanagerial.pendingloan.PendingLoanActivity
import com.skripsi.perpustakaanapp.ui.login.LoginActivity
import com.skripsi.perpustakaanapp.ui.book.listbook.BookActivity

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

        if (intent.extras!=null){
            supportActionBar?.title = "Hi, ${intent.getStringExtra("first_name")}"
        }

        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
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
        if (doubleBackPressed) {
            super.onBackPressed()
            return
        }
        doubleBackPressed = true
        Snackbar.make(binding.root, "Tekan Sekali Lagi Untuk Keluar", Snackbar.LENGTH_LONG)
            .show()
        Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackPressed = false }, 2000)
    }

    private fun userLogout() {
        sessionManager = SessionManager(this)
        viewModel.userLogout(sessionManager.fetchAuthToken().toString())
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
                R.id.card_create_book -> {
                    val intent = Intent(this@HomeAdminActivity, CreateBookActivity::class.java)
                    startActivity(intent)
                }
                R.id.card_create_admin -> {
                    val intent = Intent(this@HomeAdminActivity, CreateNewAdminActivity::class.java)
                    startActivity(intent)
                }
                R.id.card_book_list -> {
                    val intent = Intent(this@HomeAdminActivity, BookActivity::class.java)
                    startActivity(intent)
                }
                R.id.card_pending_loan_list -> {
                    val intent = Intent(this@HomeAdminActivity, PendingLoanActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        binding.cardCreateBook.setOnClickListener(clickListener)
        binding.cardCreateAdmin.setOnClickListener(clickListener)
        binding.cardBookList.setOnClickListener(clickListener)
        binding.cardPendingLoanList.setOnClickListener(clickListener)
    }
}