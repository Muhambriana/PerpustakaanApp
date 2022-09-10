package com.skripsi.perpustakaanapp.ui.admin.bookmanagerial.createbook

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.databinding.ActivityCreateBookBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.ui.setSingleClickListener
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class CreateBookActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var binding: ActivityCreateBookBinding
    private lateinit var viewModel: CreateBookViewModel

    private val client = RetrofitClient
    private val context = this@CreateBookActivity
    private var imageMultipartBody: MultipartBody.Part? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
            CreateBookViewModel::class.java
        )
        binding.progressBar.visibility = View.INVISIBLE

        binding.buttonImage.setSingleClickListener {
            permissionCheck()
        }
        binding.buttonSave.setSingleClickListener {
            askAppointment()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE) {
            val selectedImage = data?.data
            binding.imageView.setImageURI(selectedImage)
            selectedImage?.let { getImageFileByUri(it) }
        }
    }

    private fun getImageFileByUri(uri: Uri) {
        val pathColumn = arrayOf(MediaStore.Images.Media.DATA)

        val cursor =  contentResolver.query(uri, pathColumn, null, null, null)
        assert(cursor != null )
        cursor?.moveToFirst()

        val columnIndex = cursor?.getColumnIndex(pathColumn[0])
        val mediaPath = columnIndex?.let { cursor.getString(it) }
        cursor?.close()

        getImage(mediaPath)
    }

    private fun permissionCheck() {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(context, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE_PERMISSION)
        } else {
            chooseImage()
        }
    }

    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_IMAGE)
    }

    private fun getImage(mediaPath: String?) {
        val file = mediaPath?.let { File(it) }
        val requestBody = file?.let { RequestBody.create(MediaType.parse("multipart/form-data"), it) }
        imageMultipartBody = requestBody?.let { MultipartBody.Part.createFormData("image", file.name, it) }
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
        Log.v("CBA", "mantap 2$imageMultipartBody")

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
                (if (imageMultipartBody != null) {
                    imageMultipartBody
                }else {
                    null
                })
            )
            Log.v("CBA", "mantap 1"+binding.edBookTitle.toString())



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
        private const val REQUEST_CODE_IMAGE = 201
        private const val REQUEST_CODE_PERMISSION = 202
    }
}