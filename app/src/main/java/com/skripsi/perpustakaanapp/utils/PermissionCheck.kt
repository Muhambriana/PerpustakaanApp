package com.skripsi.perpustakaanapp.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


object PermissionCheck {

    private const val REQUEST_CODE_PERMISSION= 202

    fun readExternalStorage(context: Activity?): Boolean {
        if (context != null) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_PERMISSION)
                return false
            }
        }
        return true
    }

    fun camera(context: Activity?): Boolean {
        if (context != null) {
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_PERMISSION)
                return false
            }
        }
        return true
    }
}