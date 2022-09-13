package com.skripsi.perpustakaanapp.ui.admin.loanmanagerial.pendingloan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
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
import com.skripsi.perpustakaanapp.ui.book.detailbook.DetailBookActivity
import com.skripsi.perpustakaanapp.ui.userprofile.UserProfileActivity
import com.skripsi.perpustakaanapp.utils.WindowTouchableHelper

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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sessionManager = SessionManager(this)

        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
            PendingLoanViewModel::class.java
        )

        getPendingLoanData()

        onClickListener()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
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
                            this,
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

    private fun onClickListener() {

        pendingLoanAdapter.onMemberUsernameClick = { memberUsername ->
            val intent = Intent(this, UserProfileActivity::class.java)
            intent.putExtra(UserProfileActivity.USERNAME, memberUsername)
            startActivity(intent)
        }

        pendingLoanAdapter.onBookTitleClick = { bookId ->
            val intent = Intent(this, DetailBookActivity::class.java)
            intent.putExtra(DetailBookActivity.BOOK_ID, bookId)
            startActivity(intent)
        }


        buttonApproveListener()

        buttonRejectListener()
    }

    private fun buttonApproveListener() {
        pendingLoanAdapter.buttonApproveClick = { id ->
            viewModel.approveLoan(sessionManager.fetchAuthToken().toString(), id, sessionManager.fetchUsername().toString())

            viewModel.resourceApproveLoan.observe(this) { event ->
                event.getContentIfNotHandled()?.let { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            WindowTouchableHelper.disable(this)
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            WindowTouchableHelper.enable(this)
                            binding.progressBar.visibility = View.GONE
                            MyAlertDialog.showAlertDialogEvent(
                                this,
                                R.drawable.icon_checked,
                                resource.data.toString().uppercase(),
                                "Peminjaman Disetujui, Serhakan Buku Kepada Siswa"
                            ) { _,_ ->
                                getPendingLoanData()
                            }
                        }
                        is Resource.Error -> {
                            WindowTouchableHelper.enable(this)
                            binding.progressBar.visibility = View.GONE
                            MyAlertDialog.showAlertDialog(
                                this,
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
                            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                            binding.progressBar.visibility = View.GONE
                            MyAlertDialog.showAlertDialogEvent(
                                this,
                                R.drawable.icon_checked,
                                resource.data.toString().uppercase(),
                                "Peminjaman Ditolak"
                            ) { _,_ ->
                                getPendingLoanData()
                            }
                        }
                        is Resource.Error -> {
                            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                            MyAlertDialog.showAlertDialog(
                                this,
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