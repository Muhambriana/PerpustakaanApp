package com.skripsi.perpustakaanapp.ui

import android.os.SystemClock
import android.view.View

class SingleClickListener(private var defaultInterval: Int = 2000, private val onSingleClick: (View) -> Unit) : View.OnClickListener {
    private var lastTimeClicked: Long = 0

    override fun onClick(view: View?) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval)
        {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        view?.let { onSingleClick(it) }
    }
}

fun View.setSingleClickListener(onSingleClick: (View) -> Unit) {
    val singleClickListener = SingleClickListener { view ->
        onSingleClick(view)
    }
    setOnClickListener(singleClickListener)
}