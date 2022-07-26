package com.skripsi.perpustakaanapp.ui.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.models.Book
import com.skripsi.perpustakaanapp.databinding.ActivityDetailBookBinding
import com.skripsi.perpustakaanapp.ui.admin.updatebook.UpdateBookActivity

class DetailBookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBookBinding
    private var detailBook: Book? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detailBook = intent.getParcelableExtra<Book>(EXTRA_DATA)
        showDetailBook(detailBook)

        //if buku belum ada yang pinjam
        //binding.buttonLoan.isEnabled = true
        //else
        //binding.buttonLoan.isEnabled = false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_detail_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit_menu -> {
                updateBook()
                true
            }
            else -> true
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