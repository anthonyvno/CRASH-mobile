package com.example.europeesaanrijdingsformulier.profile

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
class License(@field:Json(name = "id")val id: Int ,
              @field:Json(name = "category")val category: String,
              @field:Json(name = "licenseNumber")val licenseNumber: String,
              @field:Json(name = "expires")val expires: String) : Parcelable, Serializable