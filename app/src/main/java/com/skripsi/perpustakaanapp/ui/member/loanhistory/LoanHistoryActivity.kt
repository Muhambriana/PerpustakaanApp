package com.skripsi.perpustakaanapp.ui.member.loanhistory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.adapter.LoanHistoryAdapter
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.Resource
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
    private val context = this@LoanHistoryActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoanHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
            LoanHistoryViewModel::class.java
        )

        getHistoryLoanData()

        onClickListener()

    }

    private fun onClickListener() {
        loanHistoryAdapter.onBookTitleClick = { bookId ->
            val intent = Intent(context, DetailBookActivity::class.java)
            intent.putExtra(DetailBookActivity.BOOK_ID, bookId)
            startActivity(intent)
        }
        loanHistoryAdapter.onOfficerUsernameClick = { officerUsername ->
            val intent = Intent(context, UserProfileActivity::class.java)
            intent.putExtra(UserProfileActivity.OFFICER_USERNAME, officerUsername)
            startActivity(intent)
        }
    }

    private fun getHistoryLoanData() {
        sessionManager = SessionManager(this)

        viewModel.getAllLoanHistories(sessionManager.fetchAuthToken().toString(), sessionManager.fetchUsername().toString())

        viewModel.resourceHistoryLoan.observe(this) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.rvHistoryLoan.adapter = loanHistoryAdapter
                        binding.progressBar.visibility = View.GONE
                        loanHistoryAdapter.setLoanHistoryList(resource.data)
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        MyAlertDialog.showAlertDialog(context, R.drawable.icon_cancel, "Failed", resource.message.toString())
                    }
                }
            }
        }
    }
}