package com.skripsi.perpustakaanapp.ui

import android.graphics.Color
import android.view.View
import com.google.android.material.snackbar.Snackbar

class MySnackBar {
    companion object {
        // Black Snack bar background
        fun showBlack(view: View?, message: String) {
            if (view != null) {
                Snackbar
                    .make(view, message, 7000)
                    .setBackgroundTint(Color.BLACK)
                    .setTextColor(Color.WHITE)
                    .show()
            }
        }

        // White snack bar background
        fun showWhite(view: View?, message: String) {
            if (view != null) {
                Snackbar
                    .make(view, message, 7000)
                    .show()
            }
        }

        // Red snack bar background
        fun showRed(view: View?, message: String) {
            if (view != null) {
                Snackbar
                    .make(view, message, 7000)
                    .setBackgroundTint(Color.RED)
                    .setTextColor(Color.WHITE)
                    .show()
            }
        }
    }
}