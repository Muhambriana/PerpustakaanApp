package com.skripsi.perpustakaanapp.ui.user.book

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.skripsi.perpustakaanapp.core.MViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.adapter.BookAdapter
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.databinding.ActivityBookBinding
import com.skripsi.perpustakaanapp.ui.user.DetailBookActivity

class BookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: BookViewModel

    private val client = RetrofitClient
    private val bookAdapter = BookAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.VISIBLE
        viewModel = ViewModelProvider(this, MViewModelFactory(LibraryRepository(client))).get(
                BookViewModel::class.java
            )

        getBookData()
    }

    private fun getBookData() {
        sessionManager = SessionManager(this)

        //set recyclerview adapter
        binding.recyclerview.adapter = bookAdapter

        viewModel.getAllBooks(token = "Bearer ${sessionManager.fetchAuthToken()}")

        viewModel.bookList.observe(this) {
            Log.d(TAG, "bookList: $it")
            binding.progressBar.visibility = View.GONE
            bookAdapter.setBookList(it)
        }

        viewModel.errorMessage.observe(this) {
            binding.progressBar.visibility = View.GONE
            AlertDialog.Builder(this@BookActivity)
                .setTitle("ERROR")
                .setMessage(it)
                .setPositiveButton("Tutup"){_,_ ->
                    finish()
                }
                .show()
        }

        //untuk mengirim data ke serta membuka activity detail
        bookAdapter.onItemClick = {
            val intent = Intent(this, DetailBookActivity::class.java)
            intent.putExtra(DetailBookActivity.EXTRA_DATA, it)
            startActivity(intent)
        }

    }

    override fun onRestart() {
        super.onRestart()
    }

    companion object{
        private val TAG = "BookActivity"
    }
}