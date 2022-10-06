package com.skripsi.perpustakaanapp.ui.member.favoritebook

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.adapter.BookAdapter
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyResource
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

        firstInitialization()
        getFavoriteBookData()
    }

    private fun firstInitialization() {
        supportActionBar?.title = "Buku Favorite"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
            FavoriteBookViewModel::class.java
        )
    }

    override fun supportNavigateUpTo(upIntent: Intent) {
        onBackPressed()
        super.supportNavigateUpTo(upIntent)
    }

    private fun getFavoriteBookData() {
        sessionManager = SessionManager(this)

        viewModel.getAllFavorites(token = sessionManager.fetchAuthToken().toString())

        viewModel.resourceFavorite.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
                when (resource) {
                    is MyResource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is MyResource.Success -> {
                        binding.rvBook.adapter = favBookAdapter
                        binding.progressBar.visibility = View.GONE
                        favBookAdapter.setBookList(resource.data)
                    }
                    is MyResource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        MyAlertDialog.showWith2Event(this, R.drawable.icon_cancel, resource.message.toString(), resources.getString(R.string.refresh), resources.getString(R.string.back_to_dashboard),
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