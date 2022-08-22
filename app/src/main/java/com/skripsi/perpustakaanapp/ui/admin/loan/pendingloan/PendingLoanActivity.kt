package com.skripsi.perpustakaanapp.ui.admin.loan.pendingloan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.resource.Resource
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.adapter.PendingLoanAdapter
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.databinding.ActivityPendingLoanBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog

class PendingLoanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPendingLoanBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: PendingLoanViewModel

    private val client=  RetrofitClient
    private val pendingLoanAdapter = PendingLoanAdapter()
    private val context = this@PendingLoanActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPendingLoanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
            PendingLoanViewModel::class.java
        )

        getPendingLoanData()

        buttonApproveListener()

        buttonRejectListener()
    }

    override fun onRestart() {
        getPendingLoanData()
        super.onRestart()
    }

    private fun getPendingLoanData() {
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
                        binding.progressBar.visibility = View.GONE
                        MyAlertDialog.showAlertDialog2Event(
                            context,
                            R.drawable.icon_cancel,
                            "ERROR",
                            resource.message.toString(),
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

    private fun buttonApproveListener() {
        pendingLoanAdapter.buttonApproveClick = { id ->
            viewModel.approveLoan(sessionManager.fetchAuthToken().toString(), id, sessionManager.fetchUsername().toString())

            viewModel.resourceApproveLoan.observe(this) { event ->
                event.getContentIfNotHandled()?.let { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            binding.progressBar.visibility = View.GONE
                            MyAlertDialog.showAlertDialogEvent(
                                context,
                                R.drawable.icon_checked,
                                resource.data.toString().uppercase(),
                                "Peminjaman Disetujui, Serhakan Buku Kepada Siswa"
                            ) { _,_ ->
                                getPendingLoanData()
                            }
                        }
                        is Resource.Error -> {
                            binding.progressBar.visibility = View.GONE
                            MyAlertDialog.showAlertDialog(
                                context,
                                R.drawable.icon_cancel,
                                "Failed",
                                resource.message.toString()
                            )
                        }
                    }
                }
            }
        }
    }

    private fun buttonRejectListener() {
        print("hasil token ${sessionManager.fetchAuthToken().toString()}")
        pendingLoanAdapter.buttonRejectClick = { id ->
            viewModel.rejectLoan(sessionManager.fetchAuthToken().toString(), id, sessionManager.fetchUsername().toString())

            viewModel.resourceRejectLoan.observe(this) { event ->
                event.getContentIfNotHandled()?.let { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            binding.progressBar.visibility = View.GONE
                            MyAlertDialog.showAlertDialogEvent(
                                context,
                                R.drawable.icon_checked,
                                resource.data.toString().uppercase(),
                                "Peminjaman Ditolak"
                            ) { _,_ ->
                                getPendingLoanData()
                            }
                        }
                        is Resource.Error -> {
                            MyAlertDialog.showAlertDialog(
                                context,
                                R.drawable.icon_cancel,
                                "Failed",
                                resource.message.toString()
                            )
                        }
                    }
                }
            }
        }
    }
}