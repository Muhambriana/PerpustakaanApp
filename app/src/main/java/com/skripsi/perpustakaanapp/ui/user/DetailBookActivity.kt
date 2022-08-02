package com.skripsi.perpustakaanapp.ui.user

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.models.Book
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.databinding.ActivityDetailBookBinding
import com.skripsi.perpustakaanapp.ui.admin.updatebook.UpdateBookActivity
import com.skripsi.perpustakaanapp.ui.user.book.BookActivity

class DetailBookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBookBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: DetailBookViewModel

    private var detailBook: Book? = null
    private val client = RetrofitClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detailBook = intent.getParcelableExtra<Book>(EXTRA_DATA)
        showDetailBook(detailBook)

        sessionManager = SessionManager(this)

        viewModel= ViewModelProvider(this,MViewModelFactory(LibraryRepository(client))).get(
            DetailBookViewModel::class.java
        )

        //if buku belum ada yang pinjam
        //binding.buttonLoan.isEnabled = true
        //else
        //binding.buttonLoan.isEnabled = false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (sessionManager.fetchUserRole() == "admin") {
            menuInflater.inflate(R.menu.activity_detail_menu, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit_menu -> {
                updateBook()
                true
            }
            R.id.delete_menu -> {
                deleteBook()
                true
            }
            else -> true
        }
    }

    private fun deleteBook(){
        detailBook?.bookId?.let {
            viewModel.deleteBook(token = "Bearer ${sessionManager.fetchAuthToken()}", it)
        }

        viewModel.failMessage.observe(this) { message ->
            if (message != null) {
                //Reset status value at first to prevent multitriggering
                //and to be available to trigger action again
                viewModel.failMessage.value = null
                if (message == "") {
                    AlertDialog.Builder(this@DetailBookActivity)
                        .setTitle("Success")
                        .setMessage("Data Berhasil Di Delete")
                        .setPositiveButton("Tutup") { _, _ ->
                            finish()
//                            finishActivity(10)
//                            val intent = Intent(this, BookActivity::class.java)
//                            startActivity(intent)
                        }
                        .show()
                }
                else {
                    AlertDialog.Builder(this@DetailBookActivity)
                        .setTitle("Gagal")
                        .setMessage(message)
                        .setPositiveButton("Tutup") { _, _ ->
                            // do nothing
                        }
                        .show()
                }
            }
        }
    }

    private fun updateBook() {
        val intent = Intent(this, UpdateBookActivity::class.java)
        intent.putExtra(UpdateBookActivity.EXTRA_DATA, detailBook)
        startActivity(intent)
    }

    private fun showDetailBook(detailBook: Book?) {
        detailBook?.let {
            binding.progressBar.visibility = View.GONE
            setEnableButton(detailBook.copies)
            supportActionBar?.title = detailBook.title
            binding.author.text = detailBook.author
            binding.year.text = detailBook.edition  //harusnya tahun terbit
            binding.publisher.text = detailBook.publisher
        }
    }

    private fun setEnableButton(copies: String?) {
        if (copies != "0") {
            binding.buttonLoan.setOnClickListener{
                //memasukan judul buku kedalam daftar request pinjam

            }
        } else {
            binding.buttonLoan.isEnabled = false
        }
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}