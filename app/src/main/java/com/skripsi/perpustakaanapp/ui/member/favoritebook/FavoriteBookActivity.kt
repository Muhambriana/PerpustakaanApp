package com.skripsi.perpustakaanapp.ui.member.favoritebook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.adapter.BookAdapter
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.Resource
import com.skripsi.perpustakaanapp.databinding.ActivityFavoriteBookBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.ui.book.detailbook.DetailBookActivity

class FavoriteBookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBookBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: FavoriteBookViewModel

    private val  client = RetrofitClient
    private val favBookAdapter = BookAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
            FavoriteBookViewModel::class.java
        )

        getFavoriteBookData()
    }

    private fun getFavoriteBookData() {
        sessionManager = SessionManager(this)

        viewModel.getAllFavorites(token = sessionManager.fetchAuthToken().toString())

        viewModel.resourceFavorite.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.rvBook.adapter = favBookAdapter
                        binding.progressBar.visibility = View.GONE
                        favBookAdapter.setBookList(resource.data)
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        MyAlertDialog.showAlertDialog2Event(this, R.drawable.icon_cancel, "FAILED", resource.message.toString(),
                            { _, _ ->
                                getFavoriteBookData()
                            },
                            { _,_ ->
                                finish()
                            }
                        )
                    }
                }
            }
        }

        favBookAdapter.onItemClick = {
            val intent = Intent(this, DetailBookActivity::class.java)
            intent.putExtra(DetailBookActivity.EXTRA_DATA, it)
            startActivity(intent)
        }
    }
}