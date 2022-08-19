package com.skripsi.perpustakaanapp.ui

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MyAlertDialog {
    companion object{
        //Alert Dialog
        fun showAlertDialog (context: Context?, icon: Int, title: String, message: String) {
            if (context != null) {
                MaterialAlertDialogBuilder(context)
                    .setIcon(icon)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("Tutup"){_,_ ->
                        //do nothing
                    }
                    .show()
            }
        }

        //Alert Dialog With Click Event
        fun showAlertDialogEvent (context: Context?, icon: Int, title: String, message: String, listener: DialogInterface.OnClickListener) {
            if (context != null) {
                MaterialAlertDialogBuilder(context)
                    .setIcon(icon)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("Tutup", listener)
                    .show()
            }
        }

        fun showAlertDialog2Event (context: Context?, icon: Int, title: String, message: String, listenerPositive: DialogInterface.OnClickListener, listenerNegative: DialogInterface.OnClickListener) {
            if (context != null) {
                MaterialAlertDialogBuilder(context)
                    .setIcon(icon)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("Coba Lagi", listenerPositive)
                    .setNegativeButton("Kembali Ke Dashboard", listenerNegative)
                    .show()
            }
        }
    }
}

//package com.skripsi.perpustakaanapp.ui
//
//import android.app.Activity
//import android.content.Context
//import com.google.android.material.dialog.MaterialAlertDialogBuilder
//
//class MyAlertDialog {
//    companion object{
//        fun showAlertDialog (activity: Activity, icon: Int, title: String, message: String) {
//            MaterialAlertDialogBuilder(activity)
//                .setIcon(icon)
//                .setTitle(title)
//                .setMessage(message)
//                .setPositiveButton("Tutup"){_,_ ->
//                    //clear EditText (in activity calling this function)
//                    //finishActivity
//                }
//                .show()
//        }
//    }
//}