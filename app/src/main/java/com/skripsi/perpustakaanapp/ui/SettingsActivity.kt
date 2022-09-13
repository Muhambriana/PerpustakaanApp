package com.skripsi.perpustakaanapp.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.skripsi.perpustakaanapp.core.apihelper.RetrofitClient
import com.skripsi.perpustakaanapp.core.responses.GeneralResponse
import com.skripsi.perpustakaanapp.databinding.ActivitySettingsBinding
import com.skripsi.perpustakaanapp.utils.setSingleClickListener
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private var mediaPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonImage.setSingleClickListener {
            permissionCheck()
        }
        binding.buttonSave.setSingleClickListener {
            uploadImage()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE) {
            val selectedImage = data?.data
            binding.imageView.setImageURI(selectedImage)
            selectedImage?.let { getImageFileByUri(it) }
        }
    }

    private fun getImageFileByUri(uri: Uri) {
        val pathColumn = arrayOf(MediaStore.Images.Media.DATA)

        val cursor =  contentResolver.query(uri, pathColumn, null, null, null)
        assert(cursor != null )
        cursor?.moveToFirst()

        val columnIndex = cursor?.getColumnIndex(pathColumn[0])
        mediaPath = columnIndex?.let { cursor.getString(it) }
        cursor?.close()

    }

    private fun permissionCheck() {
        if (ContextCompat.checkSelfPermission(this@SettingsActivity, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this@SettingsActivity, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE_PERMISSION)
        } else {
            chooseImage()
        }
    }

    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_IMAGE)
    }

    private fun uploadImage() {
//        val map = HashMap<String, RequestBody>()
//        val file = File(postPath!!)
//
//        val requestBody = RequestBody.create(MediaType.parse("*/*"), file)
//        map.put("image\"; filename=\""+file.name+"\"", requestBody)
//
//        val call = RetrofitClient.create().testUpload(map)
//        val file = mediaPath?.let { File(it) }
//        val requestBody = file?.let { RequestBody.create(MediaType.parse("multipart/form-data"), it) }
//        val body = requestBody?.let { MultipartBody.Part.createFormData("image", file.name, it) }
//        val call = RetrofitClient.create().testUpload(body)
//        call.enqueue(object : Callback<GeneralResponse> {
//            override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
//                if (response.body()?.code == 0) {
//                       if (response.body() != null) {
//                            Toast.makeText(applicationContext, response.body()?.message, Toast.LENGTH_SHORT).show()
//                    }
//                } else {
//                    Toast.makeText(applicationContext, response.body()?.message, Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
//                t.message?.let { Log.v("Response gotten is", it) }
//            }
//        })
    }


    companion object {
        private const val REQUEST_CODE_IMAGE = 201
        private const val REQUEST_CODE_PERMISSION = 202
    }
}