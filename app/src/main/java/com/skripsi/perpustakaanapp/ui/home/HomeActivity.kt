package com.skripsi.perpustakaanapp.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.databinding.ActivityHomeAdminBinding
import com.skripsi.perpustakaanapp.databinding.ActivityHomeBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.ui.MySnackBar
import com.skripsi.perpustakaanapp.ui.admin.bookmanagerial.updatebook.UpdateBookFragment
import com.skripsi.perpustakaanapp.ui.book.detailbook.ViewImageFragment
import com.skripsi.perpustakaanapp.ui.login.LoginActivity
import com.skripsi.perpustakaanapp.utils.WindowTouchableHelper

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var bottomNav : BottomNavigationView
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: HomeViewModel

    private var doubleBackPressed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(RetrofitClient))).get(
            HomeViewModel::class.java
        )
        setBottomNav()
    }

    private fun setBottomNav() {
        showUserFragment()
        bottomNav = binding.bottomNav
        bottomNav.setOnItemSelectedListener{
            when (it.itemId) {

                R.id.home -> {
                    showUserFragment()
                    true
                }
                R.id.message -> {
                    loadFragment(ViewImageFragment())
                    true
                }
                R.id.settings -> {
                    loadFragment(UpdateBookFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun showUserFragment() {
        sessionManager = SessionManager(this)
        if (null == sessionManager.fetchAuthToken()) {
            return MySnackBar.showRed(binding.root, "Not Allowed Here")
        }
        if (sessionManager.fetchUserRole() == "admin") {
            return loadFragment(HomeAdminFragment())

        }
        if (sessionManager.fetchUserRole() == "student") {
            return loadFragment(HomeMemberFragment())
        }
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
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
            R.id.side_bar_nav -> {
                // Code
                true
            }
            else -> true
        }
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