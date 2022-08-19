package com.skripsi.perpustakaanapp.core

import android.content.Context
import android.content.SharedPreferences
import com.skripsi.perpustakaanapp.R

class SessionManager (context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    private  val editor =  prefs.edit()
    //untuk save token
    fun saveAuthToken(token: String) {
//        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    //untuk fetch token
    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun saveUserRole(roleName: String) {
//        val editor = prefs.edit()
        editor.putString(USER_ROLE, roleName)
        editor.apply()
    }

    fun fetchUserRole(): String? {
        return prefs.getString(USER_ROLE, null)
    }

    fun saveUserName(userId: String) {
        editor.putString(USER_NAME, userId)
        editor.apply()
    }

    fun fetchUserName(): String? {
        return prefs.getString(USER_NAME, null)
    }

    companion object{
        const val USER_TOKEN = "user_token"
        const val USER_ROLE = "user_role"
        const val USER_NAME = "user_name"
    }
}