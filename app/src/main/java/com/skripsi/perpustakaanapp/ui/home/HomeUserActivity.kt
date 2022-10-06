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
import com.skripsi.perpustakaanapp.databinding.ActivityHomeUserBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.ui.SettingsActivity
import com.skripsi.perpustakaanapp.ui.book.listbook.BookActivity
import com.skripsi.perpustakaanapp.ui.login.LoginActivity
import com.skripsi.perpustakaanapp.ui.member.favoritebook.FavoriteBookActivity
import com.skripsi.perpustakaanapp.ui.member.loanhistory.LoanHistoryActivity
import com.skripsi.perpustakaanapp.ui.userprofile.UserProfileActivity
import com.skripsi.perpustakaanapp.utils.WindowTouchableHelper


class HomeUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeUserBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: HomeViewModel
    private var doubleBackPressed = false

    private val client = RetrofitClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firstInitialization()
        cardListener()

    }

    private fun firstInitialization() {
        if (intent.extras!=null){
            supportActionBar?.title = "Hi,${intent.getStringExtra("FIRST_NAME")}"
            // set avatar
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
        Snackbar
            .make(binding.root, "Tekan Sekali Lagi Untuk Keluar", Snackbar.LENGTH_LONG)
            .show()
        Handler(Looper.getMainLooper()).postDelayed({ doubleBackPressed = false }, 2000)
    }

    private fun cardListener() {
        val clickListener = View.OnClickListener {view ->
            when (view.id){
                R.id.card_book_list -> {
                    val intent = Intent(this, BookActivity::class.java)
                    startActivity(intent)
                }
                R.id.card_loan_history_member -> {
                    val intent = Intent(this, LoanHistoryActivity::class.java)
                    startActivity(intent)
                }
                R.id.card_user_profile -> {
                    val intent = Intent(this, UserProfileActivity::class.java)
                    startActivity(intent)
                }
                R.id.card_favorite_book -> {
                    val intent = Intent(this, FavoriteBookActivity::class.java)
                    startActivity(intent)
                }
                binding.cardAttendance.id -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                }
            }

        }

        binding.cardBookList.setOnClickListener(clickListener)
        binding.cardLoanHistoryMember.setOnClickListener(clickListener)
        binding.cardUserProfile.setOnClickListener(clickListener)
        binding.cardFavoriteBook.setOnClickListener(clickListener)
        binding.cardAttendance.setOnClickListener(clickListener)
    }

    private fun userLogout() {
        sessionManager = SessionManager(this)
        
        viewModel.userLogout(token = sessionManager.fetchAuthToken().toString())

        viewModel.resourceLogout.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
                when (resource) {
                    is MyResource.Loading -> {
                        WindowTouchableHelper.disable(this)
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is MyResource.Success -> {
                        WindowTouchableHelper.enable(this)
                        binding.progressBar.visibility = View.VISIBLE
                        startIntentBackToLogin()
                    }
                    is MyResource.Error -> {
                        WindowTouchableHelper.enable(this)
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