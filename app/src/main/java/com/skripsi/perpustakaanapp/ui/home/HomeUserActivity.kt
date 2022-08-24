package com.skripsi.perpustakaanapp.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.databinding.ActivityHomeUserBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.ui.login.LoginActivity
import com.skripsi.perpustakaanapp.ui.book.listbook.BookActivity
import com.skripsi.perpustakaanapp.ui.member.loanhistory.LoanHistoryActivity


class HomeUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeUserBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: HomeViewModel
    private var doubleBackPressed = false

    private val client = RetrofitClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.extras!=null){
            supportActionBar?.title = " Hi,${intent.getStringExtra("first_name")}"
        }

        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
            HomeViewModel::class.java
        )

        cardListener()
//        list.addAll(MenuResource.listResource)
//        menuAdapter =MenuAdapter(list)
//        setUpRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout_menu -> {
                userLogout()
                true
            }
            else -> true
        }
    }

    override fun onBackPressed() {
        if (doubleBackPressed) {
            super.onBackPressed()
            return
        }
        doubleBackPressed = true
        Snackbar
            .make(binding.root, "Tekan Sekali Lagi Untuk Keluar", Snackbar.LENGTH_LONG)
            .show()
        Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackPressed = false }, 2000)
    }

    private fun userLogout() {
        sessionManager = SessionManager(this)
        viewModel.userLogout(token = sessionManager.fetchAuthToken().toString())
        viewModel.isSuccess.observe(this) {
            viewModel.isSuccess.postValue(null)
            if (it == true) {
                val intent = Intent(this@HomeUserActivity, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
        viewModel.errorMessage.observe(this) {
            if (it != null) {
                viewModel.errorMessage.value = null
                MyAlertDialog.showAlertDialog(this@HomeUserActivity, R.drawable.icon_cancel, "ERROR", it)
            }
        }
    }

    private fun cardListener() {
        val clickListener = View.OnClickListener {view ->
            when (view.id){
                R.id.card_book_list -> {
                    val intent = Intent(this@HomeUserActivity, BookActivity::class.java)
                    startActivity(intent)
                }
                R.id.card_loan_history_member -> {
                    val intent = Intent(this@HomeUserActivity, LoanHistoryActivity::class.java)
                    startActivity(intent)
                }

            }

        }

        binding.cardBookList.setOnClickListener(clickListener)
        binding.cardLoanHistoryMember.setOnClickListener(clickListener)
    }

//    override fun onClick(v: View?) {
//        println("masuk bos")
//        when (v?.id) {
//            R.id.book_list -> {
//                val intent = Intent(this@HomeActivity, BookActivity::class.java)
//                startActivity(intent)
//            }
//            R.id.loan_list -> {
//                val intent = Intent(this@HomeActivity, LoanActivity::class.java)
//                startActivity(intent)
//            }
//        }
//    }

    private fun setUpRecyclerView() {
//        binding.rvMenu.apply {
//            layoutManager = GridLayoutManager(context, 2)
//            adapter = menuAdapter
//            setHasFixedSize(true)
//            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
//        }
    }
}