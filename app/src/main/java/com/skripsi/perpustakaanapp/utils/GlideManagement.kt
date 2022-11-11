package com.skripsi.perpustakaanapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.skripsi.perpustakaanapp.R

class GlideManagement (context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    private  val editor =  prefs.edit()

    private var key: String? = null

    fun updateCachePoster() {
        count()
        editor.putString(SIGNATURE_POSTER, "secret_book$key")
        editor.apply()
    }

    fun fetchCachePoster(): String? {
        return prefs.getString(SIGNATURE_POSTER, null)
    }

    fun updateCacheAvatar() {
        count()
        editor.putString(SIGNATURE_AVATAR, "secret_user$key")
        editor.apply()
    }

    fun fetchCacheAvatar(): String? {
        return prefs.getString(SIGNATURE_AVATAR, null)
    }

    private fun count() {
        key = System.currentTimeMillis().toString()
    }

    companion object{
        const val SIGNATURE_POSTER = "signature_poster"
        const val SIGNATURE_AVATAR = "signature_avatar"
    }
}