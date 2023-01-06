package com.skripsi.perpustakaanapp.utils

import com.skripsi.perpustakaanapp.BuildConfig

object NetworkInfo {
    const val BASE_URL = BuildConfig.URL
    const val BOOK_IMAGE_BASE_URL = "${BASE_URL}media/book/show/"
    const val AVATAR_IMAGE_BASE_URL = "${BASE_URL}media/avatar/show/"
    //"${BuildConfig.PROTOCOL}://${BuildConfig.IP_ADDRESS}:${BuildConfig.PORT}${BuildConfig.PARENT_PATH}"
}