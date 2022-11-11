package com.skripsi.perpustakaanapp.ui.book.detailbook

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.signature.ObjectKey
import com.google.android.material.snackbar.Snackbar
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.models.Book
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.databinding.ActivityDetailBookBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.ui.MySnackBar
import com.skripsi.perpustakaanapp.ui.ViewImageFragment
import com.skripsi.perpustakaanapp.ui.admin.bookmanagerial.updatebook.UpdateBookFragment
import com.skripsi.perpustakaanapp.ui.book.ebook.EbookActivity
import com.skripsi.perpustakaanapp.utils.NetworkInfo.BOOK_IMAGE_BASE_URL
import com.skripsi.perpustakaanapp.utils.setSingleClickListener


class DetailBookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBookBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: DetailBookViewModel

    private var detailBook: Book? = null
    private val client = RetrofitClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firstInitialization()
        setDataToModels()
    }

    private fun firstInitialization() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //when still loading the data, action bar will show nothing
        supportActionBar?.title = "Detail Buku"

        //when still loading the data, fab will invisible will show nothing
        binding.fabFavorite.visibility = View.INVISIBLE

        sessionManager = SessionManager(this)

        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
            DetailBookViewModel::class.java
        )
    }

    private fun setDataToModels() {
        if(intent.extras != null) {
            // Just receive book id
            if (intent.getStringExtra(BOOK_ID) != null) {
                setDetailBook(intent.getStringExtra(BOOK_ID).toString())
                observeStatusFavorite(intent.getStringExtra(BOOK_ID).toString())
            }

            // Receive whole data book from previous activity
            else {
                detailBook = intent.getParcelableExtra(EXTRA_DATA)
                println("kocak sangat ${detailBook?.description}")
                showDetailBook()
                observeStatusFavorite(detailBook?.bookId)
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
                MyAlertDialog.showWith2Event(
                    this,
                    null,
                    resources.getString(R.string.delete_confirmation),
                    resources.getString(R.string.confirmation_yes),
                    resources.getString(R.string.confirmation_recheck),
                    {_,_ ->
                        deleteBook()
                    }, {_,_ ->

                    })
                true
            }
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> true
        }
    }

    private fun updateBook() {
        val bundle = Bundle()
        bundle.putParcelable(UpdateBookFragment.FRAGMENT_EXTRA_DATA, detailBook)

        val bottomDialogFragment = UpdateBookFragment()
        bottomDialogFragment.arguments = bundle
        bottomDialogFragment.show(supportFragmentManager, "UpdateBookFragment")
    }

    private fun deleteBook() {
        detailBook?.bookId?.let {
            viewModel.deleteBook(token = sessionManager.fetchAuthToken().toString(), it)
        }

        viewModel.resourceDeleteBook.observe(this) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is MyResource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is MyResource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        MySnackBar.showBlack(binding.root, resource.data.toString())
                        setResult(RESULT_OK) //set return data is "RESULT_OK" after success deleted
                        finish()
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

    private fun setDetailBook(bookId: String) {
        viewModel.getDetailBook(sessionManager.fetchAuthToken().toString(), bookId)

        viewModel.resourceDetailBook.observe(this) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when(resource) {
                    is MyResource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is MyResource.Success -> {
                        detailBook = resource.data
                        showDetailBook()
                        binding.progressBar.visibility = View.GONE
                    }
                    is MyResource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        MyAlertDialog.show(this, R.drawable.icon_cancel, "Failed", resource.message.toString())
                    }
                    else -> {}
                }
            }
        }
    }

    private fun showDetailBook() {
        setEnableLoanButton()

        binding.progressBar.visibility = View.GONE
        binding.tvBookTitle.text = detailBook?.title
        binding.tvAuthor.text = detailBook?.author
        binding.tvYear.text = detailBook?.publisherDate
        binding.tvPublisher.text = detailBook?.publisher
        binding.tvDescription.text = detailBook?.description
        setBookCover()
        setEBookLink()
    }

    private fun setEBookLink() {
        if (detailBook?.eBook.equals("-") ) {
            binding.buttonEbook.isEnabled = false
            return
        }
        binding.buttonEbook.setOnClickListener {
            val intent = Intent(this@DetailBookActivity, EbookActivity::class.java)
            intent.putExtra(EbookActivity.EXTRA_LINK, detailBook?.eBook)
            startActivity(intent)
        }
    }

    private fun setBookCover() {
        if (detailBook?.imageUrl != null) {
            Glide.with(this)
                .load(BOOK_IMAGE_BASE_URL + detailBook?.imageUrl)
                // For reload image on glide from the same url
                .signature(ObjectKey(System.currentTimeMillis().toString()))
                // To show the original size of image
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .fitCenter()
                .into(binding.imageCoverBook)

            // set click listener for image when clicked
            openFullImage(detailBook?.imageUrl)
        }
    }

    private fun openFullImage(imageUrl: String?) {
        binding.imageCoverBook.setSingleClickListener {
            val bundle = Bundle()
            bundle.putString("poster", imageUrl)
            val viewImageFull = ViewImageFragment()
            viewImageFull.arguments =  bundle
            viewImageFull.show(supportFragmentManager, "ViewImageFragment")
        }
    }

    private fun setEnableLoanButton() {
        if (sessionManager.fetchUserRole() == "student") {
            if (detailBook?.stock != "0") {
                doLoan()
            } else {
                binding.buttonLoan.isEnabled = false
            }
        } else {
            binding.buttonLoan.visibility = View.INVISIBLE
        }
    }

    private fun doLoan() {
        binding.buttonLoan.setSingleClickListener {
            viewModel.createTransaction(sessionManager.fetchUsername(), detailBook?.bookId)

            viewModel.resourceLoanBook.observe(this) { event ->
                event.getContentIfNotHandled()?.let { resource ->
                    when (resource) {
                        is MyResource.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is MyResource.Success -> {
                            binding.progressBar.visibility = View.GONE
                            MySnackBar.showBlack(binding.root, resource.data.toString())
                        }
                        is MyResource.Error -> {
                            binding.progressBar.visibility = View.GONE
                            MySnackBar.showRed(binding.root, resource.message.toString())
                        }
                    }
                }
            }
        }
    }

    private fun observeStatusFavorite(bookId: String?) {
        if (sessionManager.fetchUserRole() == "student") {
            viewModel.statusFavorite(sessionManager.fetchAuthToken().toString(), sessionManager.fetchUsername().toString(), bookId)

            viewModel.isFavorite.observe(this) { event ->
                setStatusFavorite(event.getContentIfNotHandled())
            }
        } else {
            binding.fabFavorite.visibility = View.INVISIBLE
        }
    }

    private fun setStatusFavorite(isFavorite: Boolean?) {
        if (isFavorite == true) {
            binding.fabFavorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_icon_favorite_true))
            binding.fabFavorite.visibility = View.VISIBLE
            fabListener(isFavorite)
        } else if (isFavorite == false){
            binding.fabFavorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_icon_favorite_false))
            binding.fabFavorite.visibility = View.VISIBLE
            fabListener(isFavorite)
        }
    }

    private fun fabListener(isFavorite: Boolean) {
        binding.fabFavorite.setOnClickListener {
            favoriteViewModel(isFavorite)
            setStatusFavorite(!isFavorite)
        }
    }

    private fun favoriteViewModel(isFavorite: Boolean) {
        val bookId: String? =
            if (detailBook?.bookId == null) {
                intent.getStringExtra(BOOK_ID)
            } else {
                detailBook?.bookId
            }

        viewModel.changeStatusFavorite(sessionManager.fetchAuthToken().toString(), sessionManager.fetchUsername(), bookId, isFavorite)

        viewModel.resourceChangeStatusFavorite.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
                when(resource) {
                    is MyResource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is MyResource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        Snackbar
                            .make(binding.root, resource.data.toString(), Snackbar.LENGTH_LONG)
                            .show()
                        setResult(101);
                    }
                    is MyResource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Snackbar
                            .make(binding.root, resource.message.toString(), Snackbar.LENGTH_LONG)
                            .show()
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