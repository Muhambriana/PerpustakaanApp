package com.skripsi.perpustakaanapp.core.responses

import com.google.gson.annotations.SerializedName
import com.skripsi.perpustakaanapp.core.models.StatisticAdminModel
import com.skripsi.perpustakaanapp.core.models.StatisticMemberModel


data class StatisticResponse(

    @field:SerializedName("code")
    val code: Int,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("statisticAdmin")
    val statisticAdmin: StatisticAdminModel? = null,

    @field:SerializedName("statisticMember")
    val statisticMember: StatisticMemberModel? = null

)
