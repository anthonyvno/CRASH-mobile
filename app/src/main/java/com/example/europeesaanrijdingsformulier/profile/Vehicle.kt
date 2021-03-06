package com.example.europeesaanrijdingsformulier.profile

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
class Vehicle(
    @field:Json(name = "id") var id: Int,
    @field:Json(name = "country") var country: String,
    @field:Json(name = "licensePlate") var licensePlate: String,
    @field:Json(name = "brand") var brand: String,
    @field:Json(name = "model") var model: String,
    @field:Json(name = "type") var type: String,
    @field:Json(name = "insurance") var insurance: Insurance? = null
) : Serializable, Parcelable