package com.example.europeesaanrijdingsformulier.report

import com.example.europeesaanrijdingsformulier.profile.Profile
import com.squareup.moshi.Json
import java.util.*

class Report(@field:Json(name = "id")val id: Int = 1,
            // @field:Json(name = "dateReportReceived")val dateReportReceived: Date,
             @field:Json(name = "profiles")val profiles: List<Profile> = emptyList()) {


}