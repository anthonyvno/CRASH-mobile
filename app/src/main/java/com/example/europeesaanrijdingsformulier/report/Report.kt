package com.example.europeesaanrijdingsformulier.report

import com.example.europeesaanrijdingsformulier.profile.Profile
import com.squareup.moshi.Json
import java.util.*

class Report(
    @field:Json(name = "id") val id: Int = 1,
    // @field:Json(name = "dateReportReceived")val dateReportReceived: Date,
    @field:Json(name = "profiles") val profiles: List<Profile> = emptyList(),
    @field:Json(name = "dateCrash") val dateCrash: Date? = null,
    @field:Json(name = "street") val street: String? = "",
    @field:Json(name = "streetNumber") val streetNumber: String? = "",
    @field:Json(name = "postalCode") val postalCode: String? = "",
    @field:Json(name = "city") val city: String? = "",
    @field:Json(name = "country") val country: String? = ""
) {


}