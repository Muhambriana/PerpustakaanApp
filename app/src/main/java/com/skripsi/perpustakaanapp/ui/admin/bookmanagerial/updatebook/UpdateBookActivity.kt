package com.skripsi.perpustakaanapp.ui.admin.bookmanagerial.updatebook

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.models.Book
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.Resource
import com.skripsi.perpustakaanapp.databinding.ActivityUpdateBookBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.ui.book.detailbook.DetailBookActivity
import com.skripsi.perpustakaanapp.ui.setSingleClickListener

class UpdateBookActivity : BottomSheetDialogFragment() {

    private lateinit var sessionManager: SessionManager
    private lateinit var binding: ActivityUpdateBookBinding
    private lateinit var viewModel: UpdateBookViewModel

    private var dataBook: Book? = null
    private val client = RetrofitClient


    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        binding = ActivityUpdateBookBinding.inflate(LayoutInflater.from(inflater.context), container, false)


//        setHasOptionsMenu(true)

        viewModel = ViewModelProvider(this,MyViewModelFactory(LibraryRepository(client))).get(
            UpdateBookViewModel::class.java
        )

        binding.progressBar.visibility = View.GONE

        dataBook = activity?.intent?.getParcelableExtra<Book>(EXTRA_DATA)
        setEditText(dataBook)

        binding.buttonBack.setSingleClickListener {
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        }

        binding.buttonSave.setSingleClickListener {
            askAppointment()
        }

        return binding.root
    }




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
    }

    private fun askAppointment() {
        val title = binding.edBookTitle.text.toString()
//        val price = binding.edPrice.text.toString()
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
        sessionManager = SessionManager(requireActivity())

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
            binding.edRemark.text.toString()
        )

        viewModel.resourceUpdateBook.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
                when(resource) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
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
                            binding.edRemark.text.toString()
                        )

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
                        MyAlertDialog.showAlertDialog(context, R.drawable.icon_cancel, "FAILED", resource.message.toString())
                    }
                }
            }
        }
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
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