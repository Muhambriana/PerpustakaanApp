package com.skripsi.perpustakaanapp.utils

import android.app.Activity
import android.net.Uri
import android.provider.MediaStore
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

object ImageHelper {
    fun getImagePathByUri(activity: Activity?, uri: Uri): MultipartBody.Part? {
        val pathColumn = arrayOf(MediaStore.Images.Media.DATA)

        val cursor = activity?.contentResolver?.query(uri, pathColumn, null, null, null)
        assert(cursor != null)
        cursor?.moveToFirst()

        val columnIndex = cursor?.getColumnIndex(pathColumn[0])
        val imagePath = columnIndex?.let { cursor.getString(it) }
        cursor?.close()

        return getImage(imagePath)
    }

    private fun getImage(imagePath: String?): MultipartBody.Part? {
        val file = imagePath?.let { File(it) }
        val requestBody = file?.let { RequestBody.create(MediaType.parse("multipart/form-data"), it) }
        return requestBody?.let { MultipartBody.Part.createFormData("image", file.name, it) }
    }
}