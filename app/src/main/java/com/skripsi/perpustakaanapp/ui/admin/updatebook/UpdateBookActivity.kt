package com.skripsi.perpustakaanapp.ui.admin.updatebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
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
        binding.tvBookId.text = dataBook?.bookId
        binding.edBookTitle.setText(dataBook?.title)
        binding.edEdition.setText(dataBook?.edition)
        binding.edAuthor.setText(dataBook?.author)
        binding.edCopies.setText(dataBook?.copies)
        binding.edPublisher.setText(dataBook?.publisher)
        binding.edPublisherDate.setText(dataBook?.publisherDate)
        binding.edSource.setText(dataBook?.source)
        binding.edRemark.setText(dataBook?.remark)
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
            binding.tvBookId.text.toString(),
            binding.edBookTitle.text.toString(),
            binding.edEdition.text.toString(),
            binding.edAuthor.text.toString(),
            binding.edPublisher.text.toString(),
            binding.edPublisherDate.text.toString(),
            binding.edCopies.text.toString(),
            binding.edSource.text.toString(),
            binding.edRemark.text.toString()
        )

        viewModel.failMessage.observe(this) { message ->
            if(message != null) {
                //Reset status value at first to prevent multitriggering
                //and to be available to trigger action again
                viewModel.failMessage.value = null
                if(message == "") {
                    AlertDialog.Builder(this@UpdateBookActivity)
                        .setTitle("Success")
                        .setMessage("Data Berhasil Di Update")
                        .setPositiveButton("Tutup") { _, _ ->
                            finish()
                        }
                        .show()
                }
                else {
                    AlertDialog.Builder(this@UpdateBookActivity)
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

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}