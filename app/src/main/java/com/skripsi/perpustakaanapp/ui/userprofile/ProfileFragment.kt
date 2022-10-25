package com.skripsi.perpustakaanapp.ui.userprofile

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.skripsi.perpustakaanapp.R
import com.skripsi.perpustakaanapp.core.MyViewModelFactory
import com.skripsi.perpustakaanapp.core.SessionManager
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.models.User
import com.skripsi.perpustakaanapp.core.repository.LibraryRepository
import com.skripsi.perpustakaanapp.core.resource.MyResource
import com.skripsi.perpustakaanapp.databinding.FragmentProfileBinding
import com.skripsi.perpustakaanapp.ui.MyAlertDialog
import com.skripsi.perpustakaanapp.ui.MySnackBar
import com.skripsi.perpustakaanapp.ui.admin.usermanagerial.updateuser.UpdateUserFragment
import com.skripsi.perpustakaanapp.utils.ImageHelper
import com.skripsi.perpustakaanapp.utils.NetworkInfo
import com.skripsi.perpustakaanapp.utils.PermissionCheck
import com.skripsi.perpustakaanapp.utils.setSingleClickListener
import okhttp3.MultipartBody

class ProfileFragment : BottomSheetDialogFragment() {

    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: UserProfileViewModel

    private var fragmentProfileBinding: FragmentProfileBinding? = null
    private val binding get() = fragmentProfileBinding

    private val client = RetrofitClient
    private var detailUser: User? = null
    private var username: String? = null
    private var imageMultipartBody: MultipartBody.Part? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        fragmentProfileBinding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        firstInitialization()
        setDataToModels()
        setButtonListener()
    }

    private fun firstInitialization() {

        binding?.progressBar?.visibility = View.GONE

        sessionManager = SessionManager(requireContext())

        viewModel = ViewModelProvider(this, MyViewModelFactory(LibraryRepository(client))).get(
            UserProfileViewModel::class.java
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE) {
            val selectedImage = data?.data
            MyAlertDialog.showWith2Event(
                requireContext(),
                null,
                resources.getString(R.string.change_image_confirmation),
                resources.getString(R.string.confirmation_yes),
                resources.getString(R.string.confirmation_recheck),
                {_,_ ->
                    imageMultipartBody = selectedImage?.let { ImageHelper.getImagePathByUri(activity, it) }
                    uploadImage()
                }, {_,_ ->

                })
        }
    }

    private fun uploadImage() {
        viewModel.updateImage(sessionManager.fetchAuthToken().toString(), detailUser?.username.toString(), imageMultipartBody)

        viewModel.resourceUpdateImage.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
                when(resource) {
                    is MyResource.Loading -> {
                        binding?.progressBar?.visibility = View.VISIBLE
                    }
                    is MyResource.Success -> {
                        binding?.progressBar?.visibility = View.GONE
                        restartActivity()
                    }
                    is MyResource.Error -> {
                        binding?.progressBar?.visibility = View.GONE
                        MySnackBar.showRed(binding?.root, resource.message.toString())
                    }
                }
            }
        }
    }

    private fun restartActivity() {
        // Set avatar link if user update profile for the first time
        detailUser?.avatar = "${detailUser?.username}.png"

        val intent = Intent(requireContext(), UserProfileActivity::class.java)
        intent.putExtra(EXTRA_DATA, detailUser)

        // Finish this activity before restart
        activity?.finish()

        // Restart
        startActivity(intent)
    }

    private fun setButtonListener() {
        binding?.buttonUploadImage?.setSingleClickListener {
            if (PermissionCheck.readExternalStorage(activity)) {
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
        username = activity?.intent?.getStringExtra(USERNAME)
        if (activity?.intent?.extras != null){
            if (username != null) {
                setDetailUser()
            }
            else {
                detailUser = activity?.intent?.getParcelableExtra(EXTRA_DATA)
                showDetailUser()
            }
        }
    }

    private fun setDetailUser() {
        username?.let { viewModel.getDetailUser(sessionManager.fetchAuthToken().toString(), it) }

        viewModel.resourceDetailUser.observe(requireActivity()) { event ->
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
        inUserInformation()
    }



    private fun fullName(firstName: String?, lastName: String?): String {
        var first = ""
        var last = ""
        firstName?.let { first = firstName }
        lastName?.let { last = lastName }
        return "$first $last"
    }

    private fun inUserInformation() {
        binding?.imageAvatar?.let { setProfilePhoto(it) }
        binding?.tvFirstName?.text = detailUser?.firstName
        binding?.tvLastName?.text = detailUser?.lastName
        binding?.tvEmail?.text = detailUser?.email
        binding?.tvPhoneNo?.text = detailUser?.phoneNo
        binding?.tvAddress?.text = detailUser?.address
        binding?.tvGender?.text = recognizeGender()
    }

    private fun setProfilePhoto(imageView: ImageView) {
        if (detailUser?.avatar != null) {
            Glide.with(this)
                .load(NetworkInfo.AVATAR_IMAGE_BASE_URL +detailUser?.avatar)
                .signature(ObjectKey(System.currentTimeMillis().toString()))
                .centerCrop()
                .into(imageView)
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
        const val REQUEST_CODE_IMAGE = 201
    }

}