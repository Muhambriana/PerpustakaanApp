package com.skripsi.perpustakaanapp.ui.admin.scanner

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.skripsi.perpustakaanapp.databinding.ActivityScannerBinding

class ScannerActivity : AppCompatActivity(){

    private lateinit var binding: ActivityScannerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        openFragment()
    }

    private fun openFragment() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (intent.getStringExtra(SCANNER_ID) == "attendance") {
            supportActionBar?.title = "Scan Pengunjung"
            loadFragment(ScannerAttendanceFragment())
        } else if (intent.getStringExtra(SCANNER_ID) == "returning_book") {
            supportActionBar?.title = "Scan Pengembalian Buku"
            loadFragment(ScannerReturningBookFragment())
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

    companion object{
        const val SCANNER_ID = "menu_extra_data"
    }
}