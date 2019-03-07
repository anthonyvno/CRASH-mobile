package com.example.europeesaanrijdingsformulier.profile

import com.squareup.moshi.Json
import io.reactivex.annotations.Nullable


class License(@field:Json(name = "id")val id: Int ,
              @field:Json(name = "category")val category: String,
              @field:Json(name = "licenseNumber")val licenseNumber: String,
              @field:Json(name = "expires")val expires: String) {
}