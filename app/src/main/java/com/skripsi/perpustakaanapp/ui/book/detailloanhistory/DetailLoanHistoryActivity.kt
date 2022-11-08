package com.skripsi.perpustakaanapp.ui.book.detailloanhistory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.models.LoanHistory
import com.skripsi.perpustakaanapp.databinding.ActivityDetailTransactionBinding

class DetailLoanHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailTransactionBinding
    private lateinit var sessionManager: SessionManager

    private var detailLoanHistory: LoanHistory? = null
    private val client = RetrofitClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firstInitialization()
        detailLoanHistory = intent?.getParcelableExtra(EXTRA_DATA)
    }

    private fun firstInitialization() {
        supportActionBar?.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.color_actionbar_background))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        binding.buttonShowQr.setOnClickListener {
//            val bundle = Bundle()
//            bundle.putString("qr_code", detailLoanHistory?.qrCode)
//            val fragment = QRCodeFragment()
//            fragment.arguments =  bundle
//            supportFragmentManager
//                .beginTransaction()
//                .replace(binding.containerQr.id, fragment)
//                .commit()
////            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
////            transaction.replace(binding.containerQr.id, fragment).commit()
//        }
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}