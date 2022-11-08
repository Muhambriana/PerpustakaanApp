package com.skripsi.perpustakaanapp.ui.loan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.databinding.ActivityLoanBinding

class LoanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoanBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firstInitialization()
        openFragment()
    }

    private fun firstInitialization() {
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        sessionManager = SessionManager(this)
    }

    private fun openFragment() {
        if (sessionManager.fetchUserRole() == "admin") {
            loadFragment(AdminLoanFragment())
        } else if (sessionManager.fetchUserRole() == "student") {
            loadFragment(MemberLoanFragment())
        }
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.containerScanner.id,fragment)
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