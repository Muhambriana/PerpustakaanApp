package com.skripsi.perpustakaanapp.core

import android.content.Context
import android.content.SharedPreferences
import com.skripsi.perpustakaanapp.R

class SessionManager (context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    //untuk save token
    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    //untuk fetch token
    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    companion object{
        const val USER_TOKEN = "user_token"
    }
}