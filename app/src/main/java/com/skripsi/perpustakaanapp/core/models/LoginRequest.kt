package com.skripsi.perpustakaanapp.core.models

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val nis: Int,
    val password: String
    )