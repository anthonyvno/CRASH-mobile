package com.example.europeesaanrijdingsformulier.profile

import com.squareup.moshi.Json
import java.io.Serializable

class Vehicle(@field:Json(name = "id")val id: Int,
              @field:Json(name = "country")val country: String,
              @field:Json(name = "licensePlate")val licensePlate: String,
              @field:Json(name = "brand")val brand: String,
              @field:Json(name = "model")val model: String,
              @field:Json(name = "type")val type: String): Serializable {
}