package com.example.europeesaanrijdingsformulier.report

import android.os.Parcelable
import com.example.europeesaanrijdingsformulier.profile.Profile
import com.squareup.moshi.Json
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import java.io.File
import java.io.Serializable
import java.util.*

@Parcelize
class Report(
    @field:Json(name = "id") val id: Int = 1,
    // @field:Json(name = "dateReportReceived")val dateReportReceived: Date,
    @field:Json(name = "profiles") var profiles: List<Profile> = emptyList(),
    @field:Json(name = "dateCrash") val dateCrash: Date? = null,
    @field:Json(name = "street") val street: String? = "",
    @field:Json(name = "streetNumber") val streetNumber: String? = "",
    @field:Json(name = "postalCode") val postalCode: String? = "",
    @field:Json(name = "city") val city: String? = "",
    @field:Json(name = "country") val country: String? = "",
    @field: Json(name= "pdfReport") val pdfReport: String? = ""
) : Parcelable, Serializable {


}