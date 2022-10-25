package com.skripsi.perpustakaanapp.ui.admin.pendingloan

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.adapter.PendingLoanAdapter
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.databinding.ActivityPendingLoanBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.ui.MySnackBar
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

        firstInitialization()
        getPendingLoanData()
        onClickListener()
    }

    override fun supportNavigateUpTo(upIntent: Intent) {
        onBackPressed()
        super.supportNavigateUpTo(upIntent)
    }

    private fun firstInitialization() {
        supportActionBar?.title = "Antrian Peminjaman"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sessionManager = SessionManager(this)

        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
            PendingLoanViewModel::class.java
        )
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
                    is MyResource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is MyResource.Success -> {
                        binding.rvPendingLoan.adapter = pendingLoanAdapter
                        binding.progressBar.visibility = View.GONE
                        pendingLoanAdapter.setPendingLoanList(resource.data)
                    }
                    is MyResource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        MyAlertDialog.showWith2Event(
                            this,
                            R.drawable.icon_cancel,
                            resource.message.toString(),
                            resources.getString(R.string.refresh),
                            resources.getString(R.string.back_to_dashboard),
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

        approveLoanListener()
        rejectLoanListener()
    }

    private fun approveLoanListener() {
        pendingLoanAdapter.buttonApproveClick = { id ->
            MyAlertDialog.showWith2Event(
                this,
                null,
                resources.getString(R.string.loan_approval_confirmation),
                resources.getString(R.string.confirmation_yes),
                resources.getString(R.string.confirmation_recheck),
                {_,_ ->
                    postApproveLoan(id)
                }, {_,_ ->

                })
        }
    }

    private fun postApproveLoan(id: Int) {
        viewModel.approveLoan(sessionManager.fetchAuthToken().toString(), id, sessionManager.fetchUsername().toString())

        viewModel.resourceApproveLoan.observe(this) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is MyResource.Loading -> {
                        WindowTouchableHelper.disable(this)
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is MyResource.Success -> {
                        WindowTouchableHelper.enable(this)
                        binding.progressBar.visibility = View.GONE
                        MySnackBar.showBlack(binding.root, resource.data.toString())
                        getPendingLoanData()

                    }
                    is MyResource.Error -> {
                        WindowTouchableHelper.enable(this)
                        binding.progressBar.visibility = View.GONE
                        MySnackBar.showRed(binding.root, resource.message.toString())
                    }
                }
            }
        }
    }

    private fun rejectLoanListener() {
        pendingLoanAdapter.buttonRejectClick = { id ->
            MyAlertDialog.showWith2Event(
                this,
                null,
                resources.getString(R.string.loan_approval_confirmation),
                resources.getString(R.string.confirmation_yes),
                resources.getString(R.string.confirmation_recheck),
                {_,_ ->
                    postRejectLoan(id)
                }, {_,_ ->

                })
        }
    }

    private fun postRejectLoan(id: Int) {
        viewModel.rejectLoan(sessionManager.fetchAuthToken().toString(), id, sessionManager.fetchUsername().toString())

        viewModel.resourceRejectLoan.observe(this) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is MyResource.Loading -> {
                        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is MyResource.Success -> {
                        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        binding.progressBar.visibility = View.GONE
                        MySnackBar.showBlack(binding.root, resource.data.toString())
                        // Refresh list
                        getPendingLoanData()
                    }
                    is MyResource.Error -> {
                        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        MySnackBar.showRed(binding.root, resource.message.toString())
                    }
                }
            }
        }
    }
}