package com.example.europeesaanrijdingsformulier.profile

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
class Profile(@field:Json(name = "id")val id: Int,
              @field:Json(name = "firstName")val firstName: String,
              @field:Json(name = "lastName")val lastName: String,
              @field:Json(name = "email")val email: String,
              @field:Json(name="license")var license: License? = null ,
              @field:Json(name="vehicles")var vehicles:List<Vehicle>? = emptyList()) : Parcelable, Serializable {



}