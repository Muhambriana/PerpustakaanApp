package com.skripsi.perpustakaanapp.ui.book.detailbook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.models.Book
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.Resource
import com.skripsi.perpustakaanapp.databinding.ActivityDetailBookBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.ui.admin.bookmanagerial.updatebook.UpdateBookActivity
import com.skripsi.perpustakaanapp.ui.setSingleClickListener


class DetailBookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBookBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: DetailBookViewModel

    private var detailBook: Book? = null
    private val client = RetrofitClient
    private val context = this@DetailBookActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //when still loading the data, action bar will show nothing
        supportActionBar?.title = ""

        sessionManager = SessionManager(this)

        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
            DetailBookViewModel::class.java
        )

        if(intent.extras != null) {
            if (intent.getStringExtra(BOOK_ID) != null) {
                setDetailBook(intent.getStringExtra(BOOK_ID).toString())
            }
            else {
                detailBook = intent.getParcelableExtra<Book>(EXTRA_DATA)
                showDetailBook()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (sessionManager.fetchUserRole() == "admin") {
            menuInflater.inflate(R.menu.activity_detail_menu, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit_menu -> {
                updateBook()
                true
            }
            R.id.delete_menu -> {
                deleteBook()
                true
            }
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> true
        }
    }


    override fun onBackPressed() {
        Log.i("DetailActivity", "pencet kembali")
        super.onBackPressed()
    }

    private fun updateBook() {
        val bottomDialogFragment = UpdateBookActivity()
        bottomDialogFragment.show(supportFragmentManager, "UpdateBookActivity")
    }

    private fun deleteBook() {
        detailBook?.bookId?.let {
            viewModel.deleteBook(token = sessionManager.fetchAuthToken().toString(), it)
        }

        viewModel.resourceDeleteBook.observe(this) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        MyAlertDialog.showAlertDialogEvent(context,
                            R.drawable.icon_checked,
                            resource.data.toString().uppercase(),
                            "Buku Berhasil Di Hapus")
                        { _, _ ->
                            setResult(RESULT_OK) //set return data is "RESULT_OK" after success deleted
                            finish()
                        }
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        MyAlertDialog.showAlertDialog(context,
                            R.drawable.icon_cancel,
                            "FAILED",
                            resource.message.toString())
                    }
                }
            }
        }
    }

    private fun setDetailBook(bookId: String) {
        viewModel.getDetailBook(sessionManager.fetchAuthToken().toString(), bookId)

        viewModel.resourceDetailBook.observe(this) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when(resource) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        detailBook = resource.data
                        Log.d("isi detailbook",detailBook.toString())
                        showDetailBook()
                        binding.progressBar.visibility = View.GONE
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        MyAlertDialog.showAlertDialog(context, R.drawable.icon_cancel, "Failed", resource.message.toString())
                    }
                }
            }
        }
    }


    private fun showDetailBook() {
        setEnableButton()
        Log.d("DetailActivity", detailBook?.title.toString())
        binding.progressBar.visibility = View.GONE
        supportActionBar?.title = detailBook?.title
        binding.author.text = detailBook?.author
        binding.year.text = detailBook?.edition  //harusnya tahun terbit
        binding.publisher.text = detailBook?.publisher

    }

    private fun setEnableButton() {
        if (detailBook?.copies != "0") {
            doLoan()
        } else {
            binding.buttonLoan.isEnabled = false
        }
    }

    private fun doLoan() {
        binding.buttonLoan.setSingleClickListener {
            viewModel.createTransaction(sessionManager.fetchUsername(), detailBook?.bookId)

            viewModel.resourceLoanBook.observe(this) { event ->
                event.getContentIfNotHandled()?.let {
                    when (it) {
                        is Resource.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is Resource.Success -> {
                            binding.progressBar.visibility = View.GONE
                            MyAlertDialog.showAlertDialogEvent(this@DetailBookActivity,
                                R.drawable.icon_checked,
                                it.data.toString().uppercase(),
                                "Menunggu Admin")
                            { _, _ ->
                                binding.buttonLoan.isEnabled = false
                            }
                        }

                        is Resource.Error -> {
                            binding.progressBar.visibility = View.GONE
                            MyAlertDialog.showAlertDialog(this@DetailBookActivity,
                                R.drawable.icon_cancel,
                                "FAILED",
                                it.message.toString())
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
        const val BOOK_ID = "book_id"
    }
}




/*
package com.skripsi.perpustakaanapp.ui.user

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.models.Book
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.Resource
import com.skripsi.perpustakaanapp.databinding.ActivityDetailBookBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.ui.admin.book.UpdateBookActivity

class DetailBookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBookBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: DetailBookViewModel

    private var detailBook: Book? = null
    private val client = RetrofitClient

    var activityDetailBook: DetailBookActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        sessionManager = SessionManager(this)

        activityDetailBook = this@DetailBookActivity

        viewModel= ViewModelProvider(this,MViewModelFactory(LibraryRepository(client))).get(
            DetailBookViewModel::class.java
        )

        detailBook = intent.getParcelableExtra<Book>(EXTRA_DATA)
        showDetailBook()

        //if buku belum ada yang pinjam
        //binding.buttonLoan.isEnabled = true
        //else
        //binding.buttonLoan.isEnabled = false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (sessionManager.fetchUserRole() == "admin") {
            menuInflater.inflate(R.menu.activity_detail_menu, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit_menu -> {
                updateBook()
                true
            }
            R.id.delete_menu -> {
                deleteBook()
                true
            }
            else -> true
        }
    }

    private fun updateBook() {
        val b = UpdateBookActivity()
        b.show(supportFragmentManager, "Hi")
    }

    private fun deleteBook(){
        detailBook?.bookId?.let {
            viewModel.deleteBook(token = "Bearer ${sessionManager.fetchAuthToken()}", it)
        }

//        viewModel.responseMessage.observe(this) { message ->
//            if (message != null) {
////                var i = 0
////                while (i<=10) {
////                    println("nilai message = $message")
////                    i++
////                }
//                //Reset status value at first to prevent multitriggering
//                //and to be available to trigger action again
//                viewModel.responseMessage.value = null
//                if (message == "success") {
//                    MyAlertDialog.showAlertDialogEvent(this@DetailBookActivity, R.drawable.icon_checked, "SUCCESS", "Buku Berhasil Di Hapus") { _, _ ->
//                        finish()
//                    }
//                }
//                else {
//                    MyAlertDialog.showAlertDialog(this@DetailBookActivity, R.drawable.icon_cancel, "FAILED", message)
//                }
//            }
//        }

        viewModel.deleteBook.observe(this) { event ->
            event.getContentIfNotHandled()?.let {
               when (it) {
                   is  Resource.Loading -> {
                       binding.progressBar.visibility = View.VISIBLE
                   }
                   is Resource.Success -> {
                       binding.progressBar.visibility = View.GONE
                       MyAlertDialog.showAlertDialogEvent(this@DetailBookActivity, R.drawable.icon_checked, it.data.toString().uppercase(), "Buku Berhasil Di Hapus")
                       { _, _ ->
                            finish()
                       }
                   }
                   is Resource.Error -> {
                       binding.progressBar.visibility = View.GONE
                       MyAlertDialog.showAlertDialog(this@DetailBookActivity, R.drawable.icon_cancel, "FAILED", it.message.toString())
                   }
               }
            }
        }

//        viewModel.errorMessage.observe(this) {
//            if (it != null) {
//                MyAlertDialog.showAlertDialog(this@DetailBookActivity, R.drawable.icon_cancel, "ERROR", it)
//                viewModel.errorMessage.value = null
//            }
//        }
    }

    private fun showDetailBook() {
            setEnableButton()
            binding.progressBar.visibility = View.GONE
            supportActionBar?.title = detailBook?.title
            binding.author.text = detailBook?.author
            binding.year.text = detailBook?.edition  //harusnya tahun terbit
            binding.publisher.text = detailBook?.publisher

    }

    private fun setEnableButton() {
        if (detailBook?.copies != "0") {
            doLoan()
        } else {
            binding.buttonLoan.isEnabled = false
        }
    }

    private fun doLoan() {
        binding.buttonLoan.setOnClickListener {
            viewModel.createTransaction(sessionManager.fetchUserName(), detailBook?.bookId)

            println("Disini ${sessionManager.fetchUserName()}, ${detailBook?.bookId}")

//            viewModel.responseMessage2.observe(this) { messsage ->
//                if (messsage != null) {
//                    //Reset status value at first to prevent multitriggering
//                    //and to be available to trigger action again
//                    viewModel.responseMessage2.value = null
//                    if (messsage == "success") {
//                        MyAlertDialog.showAlertDialog(this@DetailBookActivity, R.drawable.icon_checked, messsage.uppercase(Locale.getDefault()), "Berhasil Dan Mohon Menunggu Persetujuan Admin")
//                        binding.buttonLoan.isEnabled = false
//                    }
//                    else {
//                        MyAlertDialog.showAlertDialog(this@DetailBookActivity, R.drawable.icon_cancel, "FAILED", messsage)
//                    }
//                }
//            }
            viewModel.loanBook.observe(this) { event ->
                event.getContentIfNotHandled()?.let {
                    when (it) {
                        is Resource.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            binding.progressBar.visibility = View.GONE
                            MyAlertDialog.showAlertDialogEvent(this@DetailBookActivity, R.drawable.icon_checked, it.data.toString().uppercase(), "Menunggu Admin")
                            { _, _ ->
                                binding.buttonLoan.isEnabled = false
                            }
                        }
                        is Resource.Error -> {
                            binding.progressBar.visibility = View.GONE
                            MyAlertDialog.showAlertDialog(this@DetailBookActivity, R.drawable.icon_cancel, "FAILED", it.message.toString())
                        }
                    }
                }
            }

//            viewModel.errorMessage.observe(this) {
//                if (it != null) {
//                    MyAlertDialog.showAlertDialog(this@DetailBookActivity, R.drawable.icon_cancel, "ERROR", "Mohon Maaf Silahkan Coba Beberapa Saat Lagi")
//                    viewModel.responseMessage.value = null
//                }
//            }
        }
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}
 */