package com.skripsi.perpustakaanapp.ui.admin.managebook.createbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.databinding.ActivityCreateBookBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog

class CreateBookActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var binding: ActivityCreateBookBinding
    private lateinit var viewModel: CreateBookViewModel

    private val client = RetrofitClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
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
            token = sessionManager.fetchAuthToken().toString(),
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

        viewModel.responseMessage.observe(this) { message ->
            println("kocak $message")
            if (message != null) {
                //Reset status value at first to prevent multitriggering
                //and to be available to trigger action again
                viewModel.responseMessage.value = null
                if (message == "success") {
                   MyAlertDialog.showAlertDialogEvent(this@CreateBookActivity, R.drawable.icon_checked, "SUCCESS", "Buku Berhasil Ditambahkan") {_, _  ->
                       //clear edit text
//                       binding.edBookTitle.text?.clear()
                   }
                } else {
                   MyAlertDialog.showAlertDialog(this@CreateBookActivity, R.drawable.icon_cancel, "FAILED", message)
                }
            }
        }

        viewModel.errorMessage.observe(this) {
            binding.progressBar.visibility = View.GONE
            if (it != null) {
                viewModel.errorMessage.value = null
                MyAlertDialog.showAlertDialog(this@CreateBookActivity, R.drawable.icon_cancel, "ERROR", it)
            }
        }

    }

    companion object {
        private val TAG = "CreateBookActivity"
    }
}