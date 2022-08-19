package com.skripsi.perpustakaanapp.core.resource

class Event<out T> (private val content: T) {
    var hasBeenHandled = false
    //make external to read but cant write
    private set

    //if want to use one time live data, its prevent to use again
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        }
        else {
            hasBeenHandled = true
            content
        }
    }

    //alternative if want to use normally live data
    fun peekContent(): T = content
}