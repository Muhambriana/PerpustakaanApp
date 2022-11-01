package com.skripsi.perpustakaanapp.ui.admin.bookmanagerial.createbook

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.databinding.ActivityCreateBookBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.ui.MySnackBar
import com.skripsi.perpustakaanapp.utils.FilePathHelper
import com.skripsi.perpustakaanapp.utils.PermissionCheck
import com.skripsi.perpustakaanapp.utils.setSingleClickListener
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class CreateBookActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var binding: ActivityCreateBookBinding
    private lateinit var viewModel: CreateBookViewModel

    private val client = RetrofitClient
    private var imageMultipartBody: MultipartBody.Part? = null
    private var pdfMultiPartBody: MultipartBody.Part? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firstInitialization()
        clickListener()
    }

    private fun firstInitialization() {
        supportActionBar?.title = "Tambah Buku Baru"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
            CreateBookViewModel::class.java
        )

        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun clickListener() {
        binding.buttonImage.setSingleClickListener {
            if(PermissionCheck.readExternalStorage(this)) {
                chooseImage()
            }
        }
        binding.buttonPdf.setSingleClickListener {
            if(PermissionCheck.readExternalStorage(this)) {
                choosePDFFIle()
            }
        }
        binding.buttonSave.setSingleClickListener {
            askAppointment()
        }
    }

    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_IMAGE)
    }

    private fun choosePDFFIle() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, REQUEST_CODE_FILE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE) {
            val selectedImage: Uri? = data?.data
            Glide.with(this)
                .load(selectedImage)
                .into(binding.imageView)
            imageMultipartBody = selectedImage?.let { FilePathHelper.getImage(this, it) }
        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_FILE) {
            val selectedPdf:Uri? = data?.data
            if (selectedPdf!= null) {
                val file: File? = FilePathHelper.getFile(this, selectedPdf)
                binding.previewPdf.fromFile(file)
                    .pages(0)
                    .spacing(0)
                    .swipeHorizontal(false)
                    .enableSwipe(false)
                    .load()
            }
            binding.textPdf.text = selectedPdf?.let { FilePathHelper.getFileName(this, it) }
            pdfMultiPartBody = selectedPdf?.let { FilePathHelper.getPDF(this, it) }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun askAppointment() {
        val title = binding.edBookTitle.text.toString()
        when {
            title.isEmpty() -> {
                binding.edBookTitle.error = "Judul Buku Tidak Boleh Kosong"
                binding.edBookTitle.requestFocus()
            }
            else -> {
                MyAlertDialog.showWith2Event(
                    this,
                    null,
                    resources.getString(R.string.data_confirmation),
                    resources.getString(R.string.confirmation_yes),
                    resources.getString(R.string.confirmation_recheck),
                    {_,_ ->
                        postBookData()
                    }, {_,_ ->

                    })
            }
        }
    }

    private fun postBookData() {
        sessionManager = SessionManager(this)

        viewModel.createBook(
            token = sessionManager.fetchAuthToken().toString(),
            binding.edBookTitle.text.toString(),
            binding.edEdition.text.toString(),
            binding.edAuthor.text.toString(),
            binding.edPublisher.text.toString(),
            binding.edPublisherDate.text.toString(),
            binding.edCopies.text.toString(),
            (if (imageMultipartBody != null) {
                imageMultipartBody
            }else {
                null
            }),
            (if (pdfMultiPartBody != null) {
                pdfMultiPartBody
            } else {
                null
            })

        )

        viewModel.resourceCreateBook.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
                when (resource) {
                    is MyResource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is MyResource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        MySnackBar.showBlack(binding.root, resource.data.toString().uppercase())
                        clearEditText()
                    }
                    is MyResource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        println("kocak error: "+resource.message)
                        MySnackBar.showRed(binding.root, resource.message.toString())
                    }
                }
            }
        }
    }

    private fun clearEditText() {
        binding.edBookTitle.text?.clear()
    }

    companion object {
        private const val REQUEST_CODE_IMAGE = 201
        private const val REQUEST_CODE_FILE = 202
        private const val TYPE_TEXT_FLAG_CAP_CHARACTERS = 4096
    }
}