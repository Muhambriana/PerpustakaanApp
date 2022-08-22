package com.skripsi.perpustakaanapp.ui.admin.loan.pendingloan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.resource.Resource
import com.skripsi.perpustakaanapp.core.MViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.adapter.PendingLoanAdapter
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.models.PendingLoan
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.databinding.ActivityPendingLoanBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog

class PendingLoanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPendingLoanBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: PendingLoanViewModel

    private val client=  RetrofitClient
    private val pendingLoanAdapter = PendingLoanAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPendingLoanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, MViewModelFactory(LibraryRepository(client))).get(
            PendingLoanViewModel::class.java
        )

        getPendingLoanData()
    }

    override fun onRestart() {
        getPendingLoanData()
        super.onRestart()
    }

    private fun getPendingLoanData() {
        sessionManager = SessionManager(this)

        viewModel.getAllPendingLoans(sessionManager.fetchAuthToken().toString())

        viewModel.resourcePendingLoan.observe(this) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.rvPendingLoan.adapter = pendingLoanAdapter
                        binding.progressBar.visibility = View.GONE
                        pendingLoanAdapter.setPendingLoanList(resource.data)
                    }
                    is Resource.Error -> {
                        MyAlertDialog.showAlertDialog2Event(this@PendingLoanActivity, R.drawable.icon_cancel, "ERROR", resource.message.toString(),
                            { _,_ ->
                                getPendingLoanData()
                            },
                            { _,_ ->
                                finish()
                            }
                        )
                    }
                }
            }
        }
    }
}