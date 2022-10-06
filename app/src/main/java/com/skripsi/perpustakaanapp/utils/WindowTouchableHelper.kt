package com.skripsi.perpustakaanapp.utils

import android.app.Activity
import android.view.WindowManager

object WindowTouchableHelper {
    fun disable(activity: Activity?) {
        activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
    fun enable(activity: Activity?) {
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
}