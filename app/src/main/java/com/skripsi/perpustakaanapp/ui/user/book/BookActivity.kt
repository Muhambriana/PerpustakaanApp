package com.skripsi.perpustakaanapp.ui.user.book

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.skripsi.perpustakaanapp.core.MViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.adapter.BookAdapter
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.databinding.ActivityBookBinding
import com.skripsi.perpustakaanapp.ui.user.DetailBookActivity


class BookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: BookViewModel

    private val client = RetrofitClient
    private val bookAdapter = BookAdapter()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.VISIBLE
        viewModel = ViewModelProvider(this, MViewModelFactory(LibraryRepository(client))).get(
                BookViewModel::class.java
            )

        getBookData()
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.activity_detail_menu, menu)
//
//        // below line is to get our menu item.
//        val searchItem: MenuItem? = menu?.findItem(R.id.search_menu)
//
//        // getting search view of our item.
//        val searchView: SearchView = searchItem?.actionView as SearchView
//
//        // below line is to call set on query text listener method.
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(p0: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(msg: String): Boolean {
//                // inside on query text change method we are
//                // calling a method to filter our recycler view.
//                filter(msg)
//                return false
//            }
//        })
//        return true
////        return super.onCreateOptionsMenu(menu)
//    }



    private fun getBookData() {
        sessionManager = SessionManager(this)

        viewModel.isLoading.observe(this) { boolean ->
            binding.progressBar.visibility = if (boolean) View.VISIBLE else View.GONE
        }
        //set recyclerview adapter
        binding.recyclerview.adapter = bookAdapter

        viewModel.getAllBooks(token = "Bearer ${sessionManager.fetchAuthToken()}")

        viewModel.bookList.observe(this) {
            Log.d(TAG, "bookList: $it")
            binding.progressBar.visibility = View.GONE
            bookAdapter.setBookList(it)
        }

        viewModel.errorMessage.observe(this) {
            binding.progressBar.visibility = View.GONE
            AlertDialog.Builder(this@BookActivity)
                .setTitle("ERROR")
                .setMessage(it)
                .setPositiveButton("Tutup"){_,_ ->
                    finish()
                }
                .show()
        }

        //untuk mengirim data ke serta membuka activity detail
        bookAdapter.onItemClick = {
            val intent = Intent(this, DetailBookActivity::class.java)
            intent.putExtra(DetailBookActivity.EXTRA_DATA, it)
//            startActivityForResult(intent,10)
            startActivity(intent)
        }

    }

    override fun onRestart() {
        getBookData()
        super.onRestart()
    }

    companion object{
        private val TAG = "BookActivity"
    }
}