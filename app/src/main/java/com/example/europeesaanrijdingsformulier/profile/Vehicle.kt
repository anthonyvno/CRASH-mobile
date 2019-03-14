package com.example.europeesaanrijdingsformulier.profile

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
class Vehicle(
    @field:Json(name = "id") val id: Int,
    @field:Json(name = "country") val country: String,
    @field:Json(name = "licensePlate") val licensePlate: String,
    @field:Json(name = "brand") val brand: String,
    @field:Json(name = "model") val model: String,
    @field:Json(name = "type") val type: String,
    @field:Json(name = "insurance") var insurance: Insurance? = null
) : Serializable, Parcelable {
}