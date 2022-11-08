package com.skripsi.perpustakaanapp.ui.pendingloan

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.databinding.ActivityPendingLoanBinding

class PendingLoanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPendingLoanBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPendingLoanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firstInitialization()
        openFragment()
    }

    private fun firstInitialization() {
        supportActionBar?.title = "Antrian Peminjaman"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        sessionManager = SessionManager(this)
    }

    private fun openFragment() {
        if (sessionManager.fetchUserRole() == "admin") {
            loadFragment(PendingLoanAdminFragment())
        } else if (sessionManager.fetchUserRole() == "student") {
            loadFragment(PendingLoanMemberFragment())
        }
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.containerPendingLoan.id,fragment)
        transaction.commit()
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
}