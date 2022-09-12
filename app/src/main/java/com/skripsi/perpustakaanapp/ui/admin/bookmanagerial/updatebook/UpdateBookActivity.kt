package com.skripsi.perpustakaanapp.ui.admin.bookmanagerial.updatebook

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.signature.ObjectKey
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.models.Book
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.Resource
import com.skripsi.perpustakaanapp.core.utils.NetworkInfo.IMAGE_URL
import com.skripsi.perpustakaanapp.databinding.ActivityUpdateBookBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.ui.book.detailbook.DetailBookActivity
import com.skripsi.perpustakaanapp.ui.setSingleClickListener
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class UpdateBookActivity : BottomSheetDialogFragment() {

    private lateinit var sessionManager: SessionManager
    private lateinit var binding: ActivityUpdateBookBinding
    private lateinit var viewModel: UpdateBookViewModel

    private var dataBook: Book? = null
    private val client = RetrofitClient
    private var imageMultipartBody: MultipartBody.Part? = null


    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        binding = ActivityUpdateBookBinding.inflate(LayoutInflater.from(inflater.context), container, false)

        sessionManager = SessionManager(requireActivity())

        viewModel = ViewModelProvider(this,MyViewModelFactory(LibraryRepository(client))).get(
            UpdateBookViewModel::class.java
        )

        binding.progressBar.visibility = View.GONE

        setData()

        setButtonListener()

        return binding.root
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE) {
            val selectedImage = data?.data
            selectedImage?.let { getImageFileByUri(it) }
        }
    }

    private fun setData() {
        dataBook = activity?.intent?.getParcelableExtra<Book>(EXTRA_DATA)
        setEditText(dataBook)
    }

    private fun setEditText(dataBook: Book?) {
        binding.tvBookId.text = dataBook?.bookId
        binding.edBookTitle.setText(dataBook?.title)
        binding.edEdition.setText(dataBook?.edition)
        binding.edAuthor.setText(dataBook?.author)
        binding.edCopies.setText(dataBook?.stock)
        binding.edPublisher.setText(dataBook?.publisher)
        binding.edPublisherDate.setText(dataBook?.publisherDate)
        binding.edSource.setText(dataBook?.source)
        binding.edRemark.setText(dataBook?.remark)
        setBookPoster(dataBook?.imageUrl)
    }

    private fun setBookPoster(imageName: String?) {
        imageName?.let {
            Glide.with(requireContext())
                .load(IMAGE_URL + imageName)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .signature(ObjectKey(System.currentTimeMillis().toString()))
                .into(binding.bookPoster)
        }
    }

    private fun setButtonListener() {
        binding.buttonBack.setSingleClickListener {
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        }

        binding.buttonSave.setSingleClickListener {
            askAppointment()
        }

        binding.buttonUploadImage.setSingleClickListener {
            chooseImage()
        }
    }

    private fun askAppointment() {
        val title = binding.edBookTitle.text.toString()
        when {
            title.isEmpty() -> {
                binding.edBookTitle.error = "Judul Buku Tidak Boleh Kosong"
                binding.edBookTitle.requestFocus()
            }
            else -> {
                postBookData()
            }
        }
    }

    private fun postBookData() {
        uploadImage()
        viewModel.updateBook(
            token = sessionManager.fetchAuthToken().toString(),
            binding.tvBookId.text.toString(),
            binding.edBookTitle.text.toString(),
            binding.edEdition.text.toString(),
            binding.edAuthor.text.toString(),
            binding.edPublisher.text.toString(),
            binding.edPublisherDate.text.toString(),
            binding.edCopies.text.toString(),
            binding.edSource.text.toString(),
            binding.edRemark.text.toString(),
            dataBook?.imageUrl.toString()
        )

        viewModel.resourceUpdateBook.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
                when(resource) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        // Set value of result
                        activity?.setResult(RESULT_OK)

                        //The new data from book
                        dataBook = Book(
                            binding.tvBookId.text.toString(),
                            binding.edBookTitle.text.toString(),
                            binding.edEdition.text.toString(),
                            binding.edAuthor.text.toString(),
                            binding.edPublisher.text.toString(),
                            binding.edPublisherDate.text.toString(),
                            binding.edCopies.text.toString(),
                            binding.edSource.text.toString(),
                            binding.edRemark.text.toString(),
                            binding.edBookTitle.text.toString().replace("\\s".toRegex(),"_")+".png"
                        )
                        println("setelah covert: "+binding.edBookTitle.text.toString().replace("\\s".toRegex(),"_")+".png")
                        // Show Alert Dialog
                        MyAlertDialog.showAlertDialogEvent(
                            context, R.drawable.icon_checked,
                            resource.data.toString().uppercase(),
                            "Data Buku Berhasil Diupdate") {_,_ ->

                            // Star Intent to DetailActivity
                            val intent = Intent(context, DetailBookActivity::class.java)
                            intent.putExtra(DetailBookActivity.EXTRA_DATA, dataBook)
                            startActivity(intent)

                            // Finish this activity(include the fragment)
                            activity?.finish()
                        }
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        MyAlertDialog.showAlertDialog(context, R.drawable.icon_cancel, "FAILED", resource.message.toString())
                    }
                }
            }
        }
    }

    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_IMAGE)
    }

    private fun getImageFileByUri(uri: Uri) {
        val pathColumn = arrayOf(MediaStore.Images.Media.DATA)

        val cursor = activity?.contentResolver?.query(uri, pathColumn, null, null, null)
        assert(cursor != null)
        cursor?.moveToFirst()

        val columnIndex = cursor?.getColumnIndex(pathColumn[0])
        val imagePath = columnIndex?.let { cursor.getString(it) }
        cursor?.close()

        // Set poster with image which chooses
        Glide.with(requireContext())
            .load(uri)
            .into(binding.bookPoster)

        getImage(imagePath)
    }

    private fun getImage(imagePath: String?) {
        val file = imagePath?.let { File(it) }
        val requestBody = file?.let { RequestBody.create(MediaType.parse("multipart/form-data"), it) }
        imageMultipartBody = requestBody?.let { MultipartBody.Part.createFormData("image", file.name, it) }
    }

    private fun uploadImage() {

        imageMultipartBody?.let { viewModel.updateBookImage(sessionManager.fetchAuthToken().toString(), dataBook?.bookId.toString(), it) }

        viewModel.resourceUpdateImage.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        MyAlertDialog.showAlertDialog(context, R.drawable.icon_checked, resource.data.toString().uppercase(), "Gambar Berhasil Diubah")
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        MyAlertDialog.showAlertDialog(context, R.drawable.icon_cancel, "Failed", resource.message.toString())
                    }
                }
            }
        }
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
        private const val REQUEST_CODE_IMAGE = 201
    }
}




//package com.skripsi.perpustakaanapp.ui.admin.updatebook
//
//import android.app.Activity
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.appcompat.app.AlertDialog
//import androidx.lifecycle.ViewModelProvider
//import com.google.android.material.bottomsheet.BottomSheetDialogFragment
//import com.google.android.material.dialog.MaterialAlertDialogBuilder
//import com.skripsi.perpustakaanapp.R
//import com.skripsi.perpustakaanapp.core.MViewModelFactory
//import com.skripsi.perpustakaanapp.core.SessionManager
//import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
//import com.skripsi.perpustakaanapp.core.models.Book
//import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
//import com.skripsi.perpustakaanapp.databinding.ActivityUpdateBookBinding
//import com.skripsi.perpustakaanapp.ui.MyAlertDialog
//import com.skripsi.perpustakaanapp.ui.user.DetailBookActivity
//
//class UpdateBookActivity : AppCompatActivity() {
//
//    private lateinit var sessionManager: SessionManager
//    private lateinit var binding: ActivityUpdateBookBinding
//    private lateinit var viewModel: UpdateBookViewModel
//
//    private var dataBook: Book? = null
//    private val client = RetrofitClient
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityUpdateBookBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//
//        viewModel = ViewModelProvider(this,MViewModelFactory(LibraryRepository(client))).get(
//            UpdateBookViewModel::class.java
//        )
//
//        binding.progressBar.visibility = View.INVISIBLE
//
//        dataBook = intent.getParcelableExtra<Book>(EXTRA_DATA)
//        setEditText(dataBook)
//
//        binding.buttonSave.setOnClickListener {
//            askAppointment()
//        }
//    }
//
//    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
//        return super.onSupportNavigateUp()
//    }
//
//    override fun onBackPressed() {
//        val intent = Intent(this@UpdateBookActivity, DetailBookActivity::class.java)
//        intent.putExtra(DetailBookActivity.EXTRA_DATA, dataBook)
//        startActivity(intent)
//        finish()
//        super.onBackPressed()
//    }
//
//    private fun setEditText(dataBook: Book?) {
//        binding.tvBookId.text = dataBook?.bookId
//        binding.edBookTitle.setText(dataBook?.title)
//        binding.edEdition.setText(dataBook?.edition)
//        binding.edAuthor.setText(dataBook?.author)
//        binding.edCopies.setText(dataBook?.copies)
//        binding.edPublisher.setText(dataBook?.publisher)
//        binding.edPublisherDate.setText(dataBook?.publisherDate)
//        binding.edSource.setText(dataBook?.source)
//        binding.edRemark.setText(dataBook?.remark)
//    }
//
//    private fun askAppointment() {
//        val title = binding.edBookTitle.text.toString()
////        val price = binding.edPrice.text.toString()
//        when {
//            title.isEmpty() -> {
//                binding.edBookTitle.error = "Judul Buku Tidak Boleh Kosong"
//                binding.edBookTitle.requestFocus()
//            }
////            price.isEmpty() -> {
////                binding.edPrice.error = "Harga Buku Tidak Boleh Kosong"
////                binding.edPrice.requestFocus()
////            }
//            else -> {
//                postBookData()
//            }
//        }
//    }
//
//    private fun postBookData() {
//        sessionManager = SessionManager(this)
//
//        viewModel.isLoading.observe(this) { boolean ->
//            binding.progressBar.visibility = if (boolean) View.VISIBLE else View.GONE
//        }
//
//        viewModel.updateBook(
//            token = "Bearer ${sessionManager.fetchAuthToken()}",
//            binding.tvBookId.text.toString(),
//            binding.edBookTitle.text.toString(),
//            binding.edEdition.text.toString(),
//            binding.edAuthor.text.toString(),
//            binding.edPublisher.text.toString(),
//            binding.edPublisherDate.text.toString(),
//            binding.edCopies.text.toString(),
//            binding.edSource.text.toString(),
//            binding.edRemark.text.toString()
//        )
//
//        viewModel.failMessage.observe(this) { message ->
//            if(message != null) {
//                //Reset status value at first to prevent multitriggering
//                //and to be available to trigger action again
//                viewModel.failMessage.value = null
//                if(message == "") {
//                    dataBook = Book(
//                        binding.tvBookId.text.toString(),
//                        binding.edBookTitle.text.toString(),
//                        binding.edEdition.text.toString(),
//                        binding.edAuthor.text.toString(),
//                        binding.edPublisher.text.toString(),
//                        binding.edPublisherDate.text.toString(),
//                        binding.edCopies.text.toString(),
//                        binding.edSource.text.toString(),
//                        binding.edRemark.text.toString()
//                    )
//                    MyAlertDialog.showAlertDialogEvent(
//                        this@UpdateBookActivity, R.drawable.icon_checked, "SUCCESS", "Data Berhasil Di Update"){_, _ ->
//                        val intent = Intent(this@UpdateBookActivity, DetailBookActivity::class.java)
//                        intent.putExtra(DetailBookActivity.EXTRA_DATA, dataBook)
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
//                        startActivity(intent)
//                        finish()
//                    }
//                }
//                else {
//                    AlertDialog.Builder(this@UpdateBookActivity)
//                        .setTitle("Gagal")
//                        .setMessage(message)
//                        .setPositiveButton("Tutup") { _, _ ->
//                            // do nothing
//                        }
//                        .show()
//                }
//            }
//        }
//    }
//
//    companion object {
//        const val EXTRA_DATA = "extra_data"
//    }
//}