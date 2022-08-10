package com.skripsi.perpustakaanapp.ui

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MyAlertDialog {
    companion object{
        //Alert Dialog
        fun showAlertDialog (activity: Activity, icon: Int, title: String, message: String) {
            MaterialAlertDialogBuilder(activity)
                .setIcon(icon)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Tutup"){_,_ ->
                    //do nothing
             }
                .show()
        }

        //Alert Dialog With Click Event
        fun showAlertDialogEvent (activity: Activity, icon: Int, title: String, message: String, listener: DialogInterface.OnClickListener) {
            MaterialAlertDialogBuilder(activity)
                .setIcon(icon)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Tutup", listener)
//                .setNegativeButton("ApA", DialogInterface.OnClickListener { dialogInterface, i ->  })
                .show()
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