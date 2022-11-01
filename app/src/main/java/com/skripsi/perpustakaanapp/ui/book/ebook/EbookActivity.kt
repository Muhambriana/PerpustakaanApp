package com.skripsi.perpustakaanapp.ui.book.ebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.databinding.ActivityEbookBinding
import com.skripsi.perpustakaanapp.ui.MySnackBar

class EbookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEbookBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: EbookViewModel

    private val client = RetrofitClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEbookBinding.inflate(layoutInflater)

        // Prevent user to screen capture
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)

        setContentView(binding.root)
        firstInitialization()
    }

    private fun firstInitialization() {
        sessionManager = SessionManager(this)
        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
            EbookViewModel::class.java
        )
        val linkEBook = intent.getStringExtra(EXTRA_LINK)
        linkEBook?.let { streamPDF(it) }
    }

    private fun streamPDF(eBook: String) {

        viewModel.showPDF(sessionManager.fetchAuthToken().toString(),eBook)

        viewModel.resourcePDF.observe(this) {event ->
            event.getContentIfNotHandled().let { resource ->
                when(resource) {
                    is MyResource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is MyResource.Success -> {
                        binding.viewPdf.fromStream(resource.data).load()
                        binding.progressBar.visibility = View.GONE
                    }
                    is MyResource.Error -> {
                        MySnackBar.showRed(binding.root, resource.message.toString())
                    }
                }
            }
        }
    }

    companion object{
        const val EXTRA_LINK= "extra_link"
    }
}