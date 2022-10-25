package com.skripsi.perpustakaanapp.core.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Parcelize
data class LoanHistory(

    @SerializedName("bookId")
    val bookId: String? = null,

    @SerializedName("bookTitle")
    val bookTitle: String? = null,

    @SerializedName("releaseDate")
    val releaseDate: String? = null,

    @SerializedName("dueDate")
    val dueDate: String? = null,

    @SerializedName("status")
    val status: String? = null,

    @SerializedName("penalty")
    val penalty: BigDecimal? = null,

    @SerializedName("managedBy")
    val managedBy: String? = null,

    @SerializedName("qrCode")
    val qrCode: String? = null
):Parcelable
