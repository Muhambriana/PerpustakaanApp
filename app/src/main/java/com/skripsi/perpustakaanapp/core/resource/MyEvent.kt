package com.skripsi.perpustakaanapp.core.resource

class MyEvent<out T> (private val content: T) {
    var hasBeenHandled = false
    // Make external to read but cant write
    private set

    // If want to use one time live data, its prevent to use again
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        }
        else {
            hasBeenHandled = true
            content
        }
    }

    // Alternative if want to use normally live data
    fun peekContent(): T = content
}