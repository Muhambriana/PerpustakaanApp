package com.skripsi.perpustakaanapp.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.databinding.ActivityHomeAdminBinding
import com.skripsi.perpustakaanapp.databinding.ActivityHomeBinding
import com.skripsi.perpustakaanapp.ui.admin.bookmanagerial.updatebook.UpdateBookFragment
import com.skripsi.perpustakaanapp.ui.book.detailbook.ViewImageFragment

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var bottomNav : BottomNavigationView
    private lateinit var sessionManager: SessionManager

    private var doubleBackPressed = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomNav()
    }

    private fun setBottomNav() {
        loadFragment(HomeAdminFragment())
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
        if (sessionManager.fetchUserRole() == "admin") {
            loadFragment(HomeAdminFragment())

        } else if (sessionManager.fetchUserRole() == "student") {
            loadFragment(HomeMemberFragment())
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
}