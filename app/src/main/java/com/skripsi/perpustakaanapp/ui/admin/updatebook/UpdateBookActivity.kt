package com.skripsi.perpustakaanapp.ui.admin.updatebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.skripsi.perpustakaanapp.core.MViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.models.Book
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.databinding.ActivityUpdateBookBinding
import com.skripsi.perpustakaanapp.ui.user.DetailBookActivity

class UpdateBookActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var binding: ActivityUpdateBookBinding
    private lateinit var viewModel: UpdateBookViewModel

    private val client = RetrofitClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this,MViewModelFactory(LibraryRepository(client))).get(
            UpdateBookViewModel::class.java
        )

        binding.progressBar.visibility = View.INVISIBLE

        val dataBook = intent.getParcelableExtra<Book>(EXTRA_DATA)
        setEditText(dataBook)

        binding.buttonSave.setOnClickListener {
            askAppointment()
        }
    }

    private fun setEditText(dataBook: Book?) {
        println("kocakk $dataBook?.title")
        binding.edBookTitle.setText(dataBook?.title)
    }

    private fun askAppointment() {
        val title = binding.edBookTitle.text.toString()
//        val price = binding.edPrice.text.toString()
        when {
            title.isEmpty() -> {
                binding.edBookTitle.error = "Judul Buku Tidak Boleh Kosong"
                binding.edBookTitle.requestFocus()
            }
//            price.isEmpty() -> {
//                binding.edPrice.error = "Harga Buku Tidak Boleh Kosong"
//                binding.edPrice.requestFocus()
//            }
            else -> {
                postBookData()
            }
        }
    }

    private fun postBookData() {
        sessionManager = SessionManager(this)

        viewModel.isLoading.observe(this) { boolean ->
            binding.progressBar.visibility = if (boolean) View.VISIBLE else View.GONE
        }

        viewModel.updateBook(
            token = "Bearer ${sessionManager.fetchAuthToken()}",
            binding.edBookTitle.text.toString(),
            binding.edEdition.text.toString(),
            binding.edAuthor.text.toString(),
            binding.edPublisher.text.toString(),
            binding.edPublisherDate.text.toString(),
            binding.edCopies.text.toString(),
            binding.edSource.text.toString(),
            binding.edRemark.text.toString(),
        )
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}