package com.skripsi.perpustakaanapp.ui.admin.bookmanagerial.createbook

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.models.BookCategory
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.databinding.ActivityCreateBookBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.ui.MySnackBar
import com.skripsi.perpustakaanapp.utils.FilePathHelper
import com.skripsi.perpustakaanapp.utils.PermissionCheck
import com.skripsi.perpustakaanapp.utils.setSingleClickListener
import okhttp3.MultipartBody
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

        binding.edBookTitle.filters += InputFilter.AllCaps()
        getCategoryData()
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
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "application/pdf"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, REQUEST_CODE_FILE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE) {
            val selectedImage: Uri? = data?.data
            if (selectedImage != null) {
                binding.contentCreate.visibility = View.VISIBLE
                binding.textImage.text = FilePathHelper.getFileName(this, selectedImage)
                Glide.with(this)
                    .load(selectedImage)
                    .into(binding.imageView)
                imageMultipartBody = selectedImage.let { FilePathHelper.getImage(this, it) }
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_FILE) {
            val selectedPdf:Uri? = data?.data
            if (selectedPdf!= null) {
                binding.contentCreate.visibility = View.VISIBLE
                binding.textPdf.text = FilePathHelper.getFileName(this, selectedPdf)

                val file: File? = FilePathHelper.getFilePDF(this, selectedPdf)
                binding.previewPdf.fromFile(file)
                    .pages(0)
                    .spacing(0)
                    .swipeHorizontal(false)
                    .enableSwipe(false)
                    .load()
                pdfMultiPartBody = FilePathHelper.getPDF(this, selectedPdf)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun askAppointment() {
        val title = binding.edBookTitle.text.toString()
        val stock = binding.edCopies.text.toString()
        when {
            title.isEmpty() -> {
                binding.edBookTitle.error = "Judul Buku Tidak Boleh Kosong"
                binding.edBookTitle.requestFocus()
            }
            stock.isEmpty() -> {
                binding.edCopies.error = "Minimal Jumlah Stock adalah 0"
                binding.edCopies.requestFocus()
            }
            binding.spinnerBookCategory.selectedItemPosition == 0 -> {
                binding.spinnerBookCategory.requestFocus()
                MySnackBar.showRed(binding.root, "Pilih Kategori Buku Terlebih Dahulu")
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
            binding.edAuthor.text.toString(),
            binding.edPublisher.text.toString(),
            binding.edPublisherDate.text.toString(),
            binding.edCopies.text.toString(),
            binding.edDescription.text.toString(),
            binding.spinnerBookCategory.selectedItem.toString(),
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
                        MySnackBar.showRed(binding.root, resource.message.toString())
                    }
                    else -> {}
                }
            }
        }
    }

    private fun clearEditText() {
        binding.edBookTitle.text?.clear()
        binding.edAuthor.text?.clear()
        binding.edPublisher.text?.clear()
        binding.edPublisherDate.text?.clear()
        binding.edCopies.text?.clear()
        binding.edDescription.text?.clear()
        binding.spinnerBookCategory.setSelection(0)
        imageMultipartBody = null
        pdfMultiPartBody = null
        binding.textImage.text = null
        binding.imageView.setImageResource(0)
        binding.textPdf.text = null
        binding.previewPdf.fromFile(null)
        binding.contentCreate.visibility = View.GONE
    }

    private fun getCategoryData(){
        sessionManager = SessionManager(this)
        viewModel.getAllBookCategory(token = sessionManager.fetchAuthToken().toString())

        viewModel.resourceBookCategory.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
                when (resource) {
                    is MyResource.Loading -> {}
                    is MyResource.Success -> {
                        prepSpinnerBookCategory(resource.data)
                    }
                    is MyResource.Error -> {
                        MySnackBar.showRed(binding.root, resource.message.toString())
                    }
                    is MyResource.Empty -> {
                        MySnackBar.showRed(binding.root, "Data Kosong")
                    }
                }
            }
        }
    }

    private fun prepSpinnerBookCategory(categoryData: List<BookCategory>?) {
        if (categoryData?.isNotEmpty() == true) {
            val listCategory: MutableList<String?> = ArrayList()
            listCategory.add("Pilih Kategori Buku")
            for (element in categoryData) {
                listCategory.add(element.categoryName)
            }
            val adapter = ArrayAdapter(this,
                R.layout.custom_spinner_text,
                listCategory)
            adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)
            binding.spinnerBookCategory.adapter = adapter
        }
    }

    companion object {
        const val REQUEST_CODE_IMAGE = 201
        const val REQUEST_CODE_FILE = 202
    }
}