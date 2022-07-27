package com.skripsi.perpustakaanapp.ui.admin.createbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.skripsi.perpustakaanapp.core.MViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.databinding.ActivityCreateBookBinding
import com.skripsi.perpustakaanapp.ui.user.book.BookViewModel

class CreateBookActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var binding: ActivityCreateBookBinding
    private lateinit var viewModel: CreateBookViewModel

    private val client = RetrofitClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, MViewModelFactory(LibraryRepository(client))).get(
            CreateBookViewModel::class.java
        )
        binding.progressBar.visibility = View.INVISIBLE

        binding.buttonSave.setOnClickListener {
            askAppointment()
        }
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

        viewModel.createBook(
            token = "Bearer ${sessionManager.fetchAuthToken()}",
            binding.edBookTitle.text.toString(),
            binding.edEdition.text.toString(),
            binding.edAuthor.text.toString(),
            binding.edPublisher.text.toString(),
            binding.edPublisherDate.text.toString(),
            binding.edCopies.text.toString(),
            binding.edSource.text.toString(),
            binding.edRemark.text.toString(),
//            binding.edPrice.text.toString().toDouble()
        )

        viewModel.failMessage.observe(this) {
            if (it == "") {
                AlertDialog.Builder(this@CreateBookActivity)
                    .setTitle("Success")
                    .setMessage("Data Berhasil Ditambahkan")
                    .setPositiveButton("Tutup") { _, _ ->
                        binding.edBookTitle.text?.clear()
                        //clear semua edit text
                    }
                    .show()
            }
            else {
                AlertDialog.Builder(this@CreateBookActivity)
                    .setTitle("Data Gagal Ditambahkan")
                    .setMessage(it)
                    .setPositiveButton("Tutup") { _, _ ->
                        //Do nothing
                    }
                    .show()
            }
        }

        viewModel.errorMessage.observe(this) {
            binding.progressBar.visibility = View.GONE
            AlertDialog.Builder(this@CreateBookActivity)
                .setTitle("ERROR")
                .setMessage(it)
                .setPositiveButton("Tutup"){_,_ ->
                    finish()
                }
                .show()
        }

    }

    companion object {
        private val TAG = "CreateBookActivity"
    }
}