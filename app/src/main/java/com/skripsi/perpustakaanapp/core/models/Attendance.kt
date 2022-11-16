package com.skripsi.perpustakaanapp.core.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.time.Clock
import java.util.*

@Parcelize
data class Attendance(

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("username")
    val user: String? = null,

    @field:SerializedName("date")
    val date: String? = null,

    @field:SerializedName("clockIn")
    val clockIn: String? = null,

    @field:SerializedName("clockOut")
    val clockOut: String? = null,

    @field:SerializedName("admin")
    val admin: String? = null

): Parcelable
