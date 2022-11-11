package com.skripsi.perpustakaanapp.ui.userprofile

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.models.User
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.databinding.ActivityUserProfileBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.ui.MySnackBar
import com.skripsi.perpustakaanapp.ui.ViewImageFragment
import com.skripsi.perpustakaanapp.ui.admin.usermanagerial.updateuser.UpdateUserFragment
import com.skripsi.perpustakaanapp.utils.FilePathHelper
import com.skripsi.perpustakaanapp.utils.GlideManagement
import com.skripsi.perpustakaanapp.utils.NetworkInfo.AVATAR_IMAGE_BASE_URL
import com.skripsi.perpustakaanapp.utils.PermissionCheck
import com.skripsi.perpustakaanapp.utils.setSingleClickListener
import okhttp3.MultipartBody

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: UserProfileViewModel
    private lateinit var glideManagement: GlideManagement

    private val client = RetrofitClient
    private var detailUser: User? = null
    private var username: String? = null
    private var imageMultipartBody: MultipartBody.Part? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firstInitialization()
        setDataToModels()
        setButtonListener()
    }

    private fun firstInitialization() {
        //when still loading the data, action bar will show nothing
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.progressBar.visibility = View.GONE

        sessionManager = SessionManager(this)

        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
            UserProfileViewModel::class.java
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_IMAGE) {
            val selectedImage = data?.data
            MyAlertDialog.showWith2Event(
                this,
                null,
                resources.getString(R.string.change_image_confirmation),
                resources.getString(R.string.confirmation_yes),
                resources.getString(R.string.confirmation_recheck),
                {_,_ ->
                    imageMultipartBody = selectedImage?.let { FilePathHelper.getImage(this, it) }
                    if (detailUser?.username != null) {
                        uploadImage(detailUser?.username.toString())
                    } else if (username != null) {
                        uploadImage(username.toString())
                    }
                }, {_,_ ->

                })
        }
    }

    private fun uploadImage(user: String) {
        glideManagement = GlideManagement(this)

        viewModel.updateImage(sessionManager.fetchAuthToken().toString(), user, imageMultipartBody)

        viewModel.resourceUpdateImage.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
                when(resource) {
                    is MyResource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is MyResource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        glideManagement.updateCacheAvatar()
                        setResult(RESULT_OK)
                        restartActivity(user)
                    }
                    is MyResource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        MySnackBar.showRed(binding.root, resource.message.toString())
                    }
                }
            }
        }
    }

    private fun restartActivity(u: String) {
        println("kocak isi usewrname: $u")
        val intent = Intent(this, UserProfileActivity::class.java)
        intent.putExtra(USERNAME, u)

        // Finish this activity before restart
        finish()

        // Restart
        startActivity(intent)
    }

    private fun setButtonListener() {
        binding.buttonUploadImage.setSingleClickListener {
            if (PermissionCheck.readExternalStorage(this)) {
                chooseImage()
            }
        }
    }

    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = ("image/*")
        startActivityForResult(intent, REQUEST_CODE_IMAGE)
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
                }
            }
        }
    }

    private fun updateMember(){
        val bundle = Bundle()
        bundle.putParcelable(UpdateUserFragment.FRAGMENT_EXTRA_DATA, detailUser)

        val bottomDialogFragment = UpdateUserFragment()
        bottomDialogFragment.arguments = bundle
        bottomDialogFragment.show(supportFragmentManager, "UpdateUserFragment")
    }

    private fun setDetailUser() {
        username?.let { viewModel.getDetailUser(sessionManager.fetchAuthToken().toString(), it) }

        viewModel.resourceDetailUser.observe(this) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is MyResource.Loading -> {
                        //progress bar
                    }
                    is MyResource.Success -> {
                        detailUser = resource.data
                        println("ini isi avatar ${detailUser?.avatar}")
                        showDetailUser()
                    }
                }
            }
        }
    }

    private fun showDetailUser() {
        inMemberCard()
        inUserInformation()
    }

    private fun inMemberCard() {
        setProfilePhoto(binding.imageAvatarInCard)
        binding.tvUsernameInCard.text = detailUser?.username
        binding.tvFullNameInCard.text = fullName(detailUser?.firstName, detailUser?.lastName)
        binding.tvGenderInCard.text = recognizeGender()
        binding.tvPhoneNoInCard.text = detailUser?.phoneNo
        binding.tvAddressInCard.text = detailUser?.address
    }

    private fun fullName(firstName: String?, lastName: String?): String {
        var first = ""
        var last = ""
        firstName?.let { first = firstName }
        lastName?.let { last = lastName }
        return "$first $last"
    }

    private fun inUserInformation() {
        setProfilePhoto(binding.imageAvatar)
        binding.tvFirstName.text = detailUser?.firstName
        binding.tvLastName.text = detailUser?.lastName
        binding.tvEmail.text = detailUser?.email
        binding.tvPhoneNo.text = detailUser?.phoneNo
        binding.tvAddress.text = detailUser?.address
        binding.tvGender.text = recognizeGender()
    }

    private fun setProfilePhoto(imageView: ImageView) {
        if (detailUser?.avatar != null) {
            Glide.with(this)
                .load(AVATAR_IMAGE_BASE_URL+detailUser?.avatar)
                .signature(ObjectKey(System.currentTimeMillis().toString()))
                .centerCrop()
                .into(imageView)

            openFullImage(detailUser?.avatar)
        }
    }

    private fun openFullImage(imageUrl: String?) {
        binding.imageAvatarInCard.setSingleClickListener {
            val bundle = Bundle()
            bundle.putString("avatar", imageUrl)
            val viewImageFull = ViewImageFragment()
            viewImageFull.arguments =  bundle
            viewImageFull.show(supportFragmentManager, "ViewImageFragment")
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
                MyAlertDialog.showWith2Event(
                    this,
                    null,
                    resources.getString(R.string.delete_confirmation),
                    resources.getString(R.string.confirmation_yes),
                    resources.getString(R.string.confirmation_recheck),
                    {_,_ ->
                        deleteMember()
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

    companion object {
        const val EXTRA_DATA = "extra_data"
        const val USERNAME = "username"
        const val REQUEST_CODE_IMAGE = 201
    }
}