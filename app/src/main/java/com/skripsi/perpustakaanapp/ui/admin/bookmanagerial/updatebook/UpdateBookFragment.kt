package com.skripsi.perpustakaanapp.ui.admin.bookmanagerial.updatebook

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.databinding.FragmentUpdateBookBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.ui.MySnackBar
import com.skripsi.perpustakaanapp.ui.admin.bookmanagerial.createbook.CreateBookActivity
import com.skripsi.perpustakaanapp.ui.book.detailbook.DetailBookActivity
import com.skripsi.perpustakaanapp.utils.FilePathHelper
import com.skripsi.perpustakaanapp.utils.NetworkInfo.BOOK_IMAGE_BASE_URL
import com.skripsi.perpustakaanapp.utils.PermissionCheck
import com.skripsi.perpustakaanapp.utils.setSingleClickListener
import okhttp3.MultipartBody
import java.io.File

class UpdateBookFragment : BottomSheetDialogFragment() {

    private lateinit var sessionManager: SessionManager
    private lateinit var binding: FragmentUpdateBookBinding
    private lateinit var viewModel: UpdateBookViewModel

    private var dataBook: Book? = null
    private val client = RetrofitClient
    private var imageMultipartBody: MultipartBody.Part? = null
    private var pdfMultiPartBody: MultipartBody.Part? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View {
        binding = FragmentUpdateBookBinding.inflate(LayoutInflater.from(inflater.context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sessionManager = SessionManager(requireActivity())

        viewModel = ViewModelProvider(this,MyViewModelFactory(LibraryRepository(client))).get(
            UpdateBookViewModel::class.java
        )

        binding.edBookTitle.filters += InputFilter.AllCaps()
        binding.progressBar.visibility = View.GONE

        getData()
        setButtonListener()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == CreateBookActivity.REQUEST_CODE_IMAGE) {
            val selectedImage: Uri? = data?.data
            Glide.with(this)
                .load(selectedImage)
                .into(binding.bookPoster)
            imageMultipartBody = selectedImage?.let { FilePathHelper.getImage(requireContext(), it) }
        } else if (resultCode == Activity.RESULT_OK && requestCode == CreateBookActivity.REQUEST_CODE_FILE) {
            val selectedPdf: Uri? = data?.data
            if (selectedPdf!= null) {
                val file: File? = FilePathHelper.getFile(requireContext(), selectedPdf)
                binding.previewPdf.background = null
                binding.previewPdf.fromFile(file)
                    .pages(0)
                    .spacing(0)
                    .swipeHorizontal(false)
                    .enableSwipe(false)
                    .load()
            }
//            binding.textPdf.text = selectedPdf?.let { FilePathHelper.getFileName(this, it) }
            pdfMultiPartBody = selectedPdf?.let { FilePathHelper.getPDF(requireContext(), it) }
        }
    }

    private fun getData() {
        val bundle: Bundle? = this.arguments
        dataBook = bundle?.getParcelable(FRAGMENT_EXTRA_DATA)
        setEditText()
    }

    private fun setEditText() {
        binding.tvBookId.text = dataBook?.bookId
        binding.edBookTitle.setText(dataBook?.title)
        binding.edAuthor.setText(dataBook?.author)
        binding.edCopies.setText(dataBook?.stock)
        binding.edPublisher.setText(dataBook?.publisher)
        binding.edPublisherDate.setText(dataBook?.publisherDate)
        setBookPoster(dataBook?.imageUrl)
    }

    private fun setBookPoster(imageName: String?) {
        imageName?.let {
            Glide.with(requireContext())
                .load(BOOK_IMAGE_BASE_URL + imageName)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .signature(ObjectKey(System.currentTimeMillis().toString()))
                .into(binding.bookPoster)
        }
    }

    private fun setButtonListener() {
        binding.buttonBack.setSingleClickListener {
            dismiss()
        }

        binding.buttonSave.setSingleClickListener {
            askAppointment()
        }

        binding.buttonUploadImage.setSingleClickListener {
            if (PermissionCheck.readExternalStorage(activity)) {
                chooseImage()
            }
        }
        binding.buttonChooseEbook.setSingleClickListener {
            if (PermissionCheck.readExternalStorage(activity)) {
                choosePDFFIle()
            }
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
                MyAlertDialog.showWith2Event(
                    requireContext(),
                    null,
                    resources.getString(R.string.data_confirmation),
                    resources.getString(R.string.confirmation_yes),
                    resources.getString(R.string.confirmation_recheck),
                    {_,_ ->
                        postBookData()
                    }, {_,_ ->

                    })
            }
        }
    }

    private fun choosePDFFIle() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, CreateBookActivity.REQUEST_CODE_FILE)
    }

    private fun postBookData() {
        uploadImage()
        uploadEBook()
        viewModel.updateBook(
            token = sessionManager.fetchAuthToken().toString(),
            binding.tvBookId.text.toString(),
            binding.edBookTitle.text.toString(),
            binding.edAuthor.text.toString(),
            binding.edPublisher.text.toString(),
            binding.edPublisherDate.text.toString(),
            binding.edCopies.text.toString(),
            if (dataBook?.imageUrl != null) {
                binding.edBookTitle.text.toString()
            } else {
                null
            }
        )

        viewModel.resourceUpdateBook.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
                when(resource) {
                    is MyResource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is MyResource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        // Set value of result
                        activity?.setResult(RESULT_OK)

                        // Show Snack bar
                        MySnackBar.showBlack(binding.root, resource.data.toString())

                        // Star Intent to DetailActivity
                        val intent = Intent(context, DetailBookActivity::class.java)
                        intent.putExtra(DetailBookActivity.BOOK_ID, dataBook?.bookId)
                        startActivity(intent)

                        // Finish this activity(include the fragment)
                        activity?.finish()

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

    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_IMAGE)
    }

    private fun uploadImage() {

        imageMultipartBody?.let { viewModel.updateBookImage(sessionManager.fetchAuthToken().toString(), dataBook?.bookId.toString(), it) }

        viewModel.resourceUpdateImage.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
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
                    else -> {}
                }
            }
        }
    }

    private fun uploadEBook() {

        pdfMultiPartBody?.let { viewModel.updateEBook(sessionManager.fetchAuthToken().toString(), dataBook?.bookId.toString(), it) }

        viewModel.resourceUpdateEBook.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
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
                    else -> {}
                }
            }
        }
    }

    companion object {
        const val FRAGMENT_EXTRA_DATA = "extra_data"
        private const val REQUEST_CODE_IMAGE = 201
        private const val REQUEST_CODE_FILE = 202
    }
}