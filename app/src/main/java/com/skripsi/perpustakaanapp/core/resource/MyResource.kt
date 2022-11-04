package com.skripsi.perpustakaanapp.core.resource

sealed class MyResource<T>(val data: T? = null, val message: String? = null) {
    class Loading<T> :MyResource<T>()
    class Success<T>(data: T?) : MyResource<T>(data)
    class Error<T>(message: String?, data: T? = null) : MyResource<T>(data, message)
    class Empty<T> : MyResource<T>()
}
