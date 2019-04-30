package com.example.europeesaanrijdingsformulier.report

import android.os.Parcelable
import com.example.europeesaanrijdingsformulier.profile.Profile
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
import java.util.*

@Parcelize
class Report(
    @field:Json(name = "id") val id: Int = 1,
    // @field:Json(name = "dateReportReceived")val dateReportReceived: Date,
    @field:Json(name = "profiles") var profiles: List<Profile> = emptyList(),
    @field:Json(name = "dateCrash") val dateCrash: Date? = null,
    @field:Json(name = "postalCode") val postalCode: String? = "",
    @field:Json(name = "city") val city: String? = "",
    @field:Json(name = "country") val country: String? = "",
    @field: Json(name = "pdfReport") var pdfReport: String? = "",
    @field: Json(name = "circumstances") var circumstances: Array<BooleanArray>? = arrayOf(
        BooleanArray(17),
        BooleanArray(17)
    ),
    @field: Json(name = "sketch") var sketch: String? = "",
    @field: Json(name = "signatures") var signatures: Array<String>? = emptyArray(),
    @field: Json(name = "pictures") var pictures: Array<String>? = emptyArray()


) : Parcelable, Serializable