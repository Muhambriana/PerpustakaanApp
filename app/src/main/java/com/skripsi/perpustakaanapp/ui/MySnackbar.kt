package com.skripsi.perpustakaanapp.ui

import android.content.Context
import android.graphics.Color
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.skripsi.perpustakaanapp.R

class MySnackbar {
    companion object {
        fun showSnackBar(view: View?, message: String) {
            if (view != null) {
                Snackbar
                    .make(view, message, Snackbar.LENGTH_LONG)
                    .setBackgroundTint(Color.BLACK)
                    .setTextColor(Color.WHITE)
                    .show()
            }
        }
    }
}