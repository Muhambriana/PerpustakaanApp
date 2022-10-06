package com.skripsi.perpustakaanapp.ui

import android.content.Context
import android.content.DialogInterface
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MyAlertDialog {
    companion object{
        //Alert Dialog
        fun show (context: Context?, icon: Int, title: String, message: String) {
            if (context != null) {
                MaterialAlertDialogBuilder(context)
                    .setIcon(icon)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("Tutup"){_,_ ->
                        //do nothing
                    }
                    .setCancelable(false)
                    .show()
            }
        }

        //Alert Dialog With Click Event
        fun showWithEvent (context: Context?, icon: Int, title: String, message: String, listener: DialogInterface.OnClickListener) {
            if (context != null) {
                MaterialAlertDialogBuilder(context)
                    .setIcon(icon)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("Tutup", listener)
                    .setCancelable(false)
                    .show()
            }
        }

        fun showWith2Event (context: Context, icon: Int?, message: String, textPositive: String, textNegative: String, listenerPositive: DialogInterface.OnClickListener, listenerNegative: DialogInterface.OnClickListener) {
            if (icon != null) {
                MaterialAlertDialogBuilder(context)
                    .setIcon(icon)
                    .setMessage(message)
                    .setPositiveButton(textPositive, listenerPositive)
                    .setNegativeButton(textNegative, listenerNegative)
                    .setCancelable(false)
                    .show()
            } else {
                MaterialAlertDialogBuilder(context)
                    .setMessage(message)
                    .setPositiveButton(textPositive, listenerPositive)
                    .setNegativeButton(textNegative, listenerNegative)
                    .setCancelable(false)
                    .show()
            }
        }
    }
}
