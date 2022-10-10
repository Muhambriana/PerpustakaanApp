package com.skripsi.perpustakaanapp.core.responses

import com.google.gson.annotations.SerializedName
import com.skripsi.perpustakaanapp.core.models.Attendance
import com.skripsi.perpustakaanapp.core.models.Book

data class ListAttendanceResponse(

    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("attendanceItems")
    val attendanceItems: List<Attendance>? = null
)
