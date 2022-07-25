package com.skripsi.perpustakaanapp.ui.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.skripsi.perpustakaanapp.core.models.Book
import com.skripsi.perpustakaanapp.databinding.ActivityDetailBookBinding

class DetailBookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBookBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailBook = intent.getParcelableExtra<Book>(EXTRA_DATA)
        showDetailBook(detailBook)

        //if buku belum ada yang pinjam
        //binding.buttonLoan.isEnabled = true
        //else
        //binding.buttonLoan.isEnabled = false
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