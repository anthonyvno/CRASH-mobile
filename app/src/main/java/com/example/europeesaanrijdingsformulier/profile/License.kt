package com.example.europeesaanrijdingsformulier.profile

import com.squareup.moshi.Json

class License(@field:Json(name = "id")val id: Int = 0,
              @field:Json(name = "category")val category: String = "",
              @field:Json(name = "licenseNumber")val licenseNumber: String = "",
              @field:Json(name = "expires")val expires: String = "") {
}