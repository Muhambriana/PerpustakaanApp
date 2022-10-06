package com.skripsi.perpustakaanapp.ui.book.listbook

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.adapter.BookAdapter
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.models.Book
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.databinding.ActivityBookBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.ui.MySnackBar
import com.skripsi.perpustakaanapp.ui.book.detailbook.DetailBookActivity


class BookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: BookViewModel
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private val client = RetrofitClient
    private val bookAdapter = BookAdapter()
    private var bookData: List<Book>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firstInitialization()
        setLauncher()
        getBookData()
    }

    override fun supportNavigateUpTo(upIntent: Intent) {
        onBackPressed()
        super.supportNavigateUpTo(upIntent)
    }

    private fun firstInitialization() {
        supportActionBar?.title = "Daftar Buku"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sessionManager = SessionManager(this)

        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
            BookViewModel::class.java
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_book_menu, menu)

        // below line is to get our menu item.
        val searchItem: MenuItem? = menu?.findItem(R.id.search_menu)

        // getting search view of our item.
        val searchView: SearchView = searchItem?.actionView as SearchView

        // below line is to call set on query text listener method.
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(title: String?): Boolean {
                searchBook(title)
                return false
            }

            override fun onQueryTextChange(msg: String): Boolean {
                return false
            }
        })

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                hideRecycleList()
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                showRecycleList(null)
                return true
            }
        })
        return true
    }


    private fun setLauncher() {
        //For get return data after launch activity
        resultLauncher =  registerForActivityResult(
            StartActivityForResult()
        ){ result ->
            if (result.resultCode == RESULT_OK) {
                //Re-run getBookData and update with the latest
                getBookData()
            }
        }
    }

    private fun getBookData() {
        viewModel.getAllBooks(sessionManager.fetchAuthToken().toString())
        viewModel.resourceBook.observe(this) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is MyResource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is MyResource.Success -> {
                        //set recyclerview adapter
                        binding.progressBar.visibility = View.GONE
                        bookData = resource.data
                        showRecycleList(null)
                    }
                    is MyResource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        MyAlertDialog.showWith2Event(this, R.drawable.icon_cancel, resource.message.toString(), resources.getString(R.string.refresh), resources.getString(R.string.back_to_dashboard),
                            { _, _ ->
                                getBookData()
                            },
                            { _,_ ->
                                finish()
                            }
                        )
                    }
                }
            }
        }
    }

    private fun hideRecycleList() {
        binding.rvBook.layoutManager = null
        binding.rvBook.adapter = null
    }

    private fun showRecycleList(dataSearch: List<Book>?) {
        binding.rvBook.layoutManager = LinearLayoutManager(this)
        binding.rvBook.adapter = bookAdapter
        if (dataSearch == null) {
            bookAdapter.setBookList(bookData)
        } else {
            bookAdapter .setBookList(dataSearch)
        }

        // On book item click
        bookItemClick()
    }

    private fun bookItemClick() {
        //untuk mengirim data ke serta membuka activity detail
        bookAdapter.onItemClick = {
            val intent = Intent(this, DetailBookActivity::class.java)
            intent.putExtra(DetailBookActivity.EXTRA_DATA, it)
            resultLauncher.launch(intent) //Launch Activity and return something
        }
    }

    private fun searchBook(title: String?) {
        viewModel.searchBook(sessionManager.fetchAuthToken().toString(), title)

        viewModel.resourceSearchBook.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
                when(resource) {
                    is MyResource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is MyResource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        showRecycleList(resource.data)
                    }
                    is MyResource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        MySnackBar.showRed(binding.root, resource.message.toString())
                        showRecycleList(resource.data)
                    }
                }
            }
        }
    }

}