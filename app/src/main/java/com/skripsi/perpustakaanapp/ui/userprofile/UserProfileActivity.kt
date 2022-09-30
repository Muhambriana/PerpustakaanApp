package com.skripsi.perpustakaanapp.ui.userprofile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.models.User
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.Resource
import com.skripsi.perpustakaanapp.databinding.ActivityUserProfileBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.utils.NetworkInfo.AVATAR_IMAGE_BASE_URL

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: UserProfileViewModel

    private var detailUser: User? = null
    private var username: String? = null
    private val client = RetrofitClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        firstInitialization()
        setDataToModels()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (sessionManager.fetchUserRole() == "admin"){
            menuInflater.inflate(R.menu.activity_detail_menu, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit_menu -> {
                updateMember()
                true
            }
            R.id.delete_menu -> {
                deleteMember()
                true
            }
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> true
        }
    }

    private fun firstInitialization() {
        //when still loading the data, action bar will show nothing
        supportActionBar?.title = ""

        sessionManager = SessionManager(this)

        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
            UserProfileViewModel::class.java
        )
    }

    private fun setDataToModels() {
        username = intent.getStringExtra(USERNAME)
        if (intent.extras != null){
            if (username != null) {
                setDetailUser()
            }
            else {
                detailUser = intent.getParcelableExtra(EXTRA_DATA)
                showDetailUser()
            }
        }
    }

    private fun deleteMember(){
        viewModel.deleteMember(sessionManager.fetchAuthToken().toString(), detailUser?.username.toString())

        viewModel.resourceDeleteMember.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
                when(resource) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        MyAlertDialog.showAlertDialogEvent(this,
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
                        MyAlertDialog.showAlertDialog(this,
                            R.drawable.icon_cancel,
                            "FAILED",
                            resource.message.toString())
                    }
                }
            }
        }
    }

    private fun updateMember(){

    }

    private fun setDetailUser() {
        username?.let { viewModel.getDetailUser(sessionManager.fetchAuthToken().toString(), it) }
        
        viewModel.resourceDetailUser.observe(this) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        //progress bar
                    }
                    is Resource.Success -> {
                        detailUser = resource.data
                        showDetailUser()
                    }
                }
            }
        }
    }

    private fun showDetailUser() {
        setProfilePhoto()
        binding.tvUsernameInCard.text = detailUser?.username
        binding.tvFullNameInCard.text = "${detailUser?.firstName} ${detailUser?.lastName}"
        binding.tvGenderInCard.text = recognizeGender()
        binding.tvPhoneNoInCard.text = detailUser?.phoneNo
        binding.tvAddressInCard.text = detailUser?.address
        binding.tvFirstName.text = detailUser?.firstName
        binding.tvLastName.text = detailUser?.lastName
        binding.tvEmail.text = detailUser?.email
        binding.textPhoneNo.text = detailUser?.phoneNo
        binding.tvAddress.text = detailUser?.address
        binding.tvGender.text = recognizeGender()
    }

    private fun setProfilePhoto() {
        if (detailUser?.avatar != null) {
            Glide.with(this)
                .load(AVATAR_IMAGE_BASE_URL+detailUser?.avatar)
                .signature(ObjectKey(System.currentTimeMillis().toString()))
                .fitCenter()
                .into(binding.imageAvatar)
        }
    }

    private fun recognizeGender(): String? {
        var result: String? = null
        when (detailUser?.gender) {
            null -> {
                result = "-"
            }
            1 -> {
                result = "Laki-Laki"
            }
            2 -> {
                result = "Perempuan"
            }
        }
        return result
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
        const val USERNAME = "username"
    }
}