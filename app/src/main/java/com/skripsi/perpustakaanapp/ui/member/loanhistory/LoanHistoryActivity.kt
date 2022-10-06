package com.skripsi.perpustakaanapp.ui.member.loanhistory

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.adapter.LoanHistoryAdapter
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.databinding.ActivityLoanHistoryBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.ui.book.detailbook.DetailBookActivity
import com.skripsi.perpustakaanapp.ui.userprofile.UserProfileActivity

class LoanHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoanHistoryBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: LoanHistoryViewModel

    private val client = RetrofitClient
    private val loanHistoryAdapter = LoanHistoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoanHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firstInitialization()
        getHistoryLoanData()
        onClickListener()
    }

    private fun firstInitialization() {
        supportActionBar?.title = "History Peminjaman"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
            LoanHistoryViewModel::class.java
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun onClickListener() {
        loanHistoryAdapter.onBookTitleClick = { bookId ->
            val intent = Intent(this, DetailBookActivity::class.java)
            intent.putExtra(DetailBookActivity.BOOK_ID, bookId)
            startActivity(intent)
        }
        loanHistoryAdapter.onOfficerUsernameClick = { officerUsername ->
            val intent = Intent(this, UserProfileActivity::class.java)
            intent.putExtra(UserProfileActivity.USERNAME, officerUsername)
            startActivity(intent)
        }
    }

    private fun getHistoryLoanData() {
        sessionManager = SessionManager(this)

        viewModel.getAllLoanHistories(sessionManager.fetchAuthToken().toString(), sessionManager.fetchUsername().toString())

        viewModel.resourceHistoryLoan.observe(this) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is MyResource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is MyResource.Success -> {
                        binding.rvHistoryLoan.adapter = loanHistoryAdapter
                        binding.progressBar.visibility = View.GONE
                        loanHistoryAdapter.setLoanHistoryList(resource.data)
                    }
                    is MyResource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        MyAlertDialog.show(this, R.drawable.icon_cancel, "Failed", resource.message.toString())
                    }
                }
            }
        }
    }
}