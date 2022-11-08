package com.skripsi.perpustakaanapp.core.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class StatisticAdminModel(

    @field:SerializedName("managedBookByAdmin")
    val managedBook: Int? = null,

    @field:SerializedName("visitors")
    val visitors: Int? = null,

    @field:SerializedName("transaction")
    val transaction: Int? = null,

    @field:SerializedName("returnedBook")
    val returnedBook: Int? = null,
) : Parcelable

@Parcelize
data class StatisticMemberModel(

    @field:SerializedName("ongoingTrxByMember")
    val ongoingTransaction: Int? = null,

    @field:SerializedName("rejectedTrxByMember")
    val rejectedTransaction: Int? = null,

    @field:SerializedName("allTrxByMember")
    val history: Int? = null,

    @field:SerializedName("overdueTrxByMember")
    val overdueTransaction: Int? = null,
) : Parcelable
