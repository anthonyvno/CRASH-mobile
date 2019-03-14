package com.example.europeesaanrijdingsformulier.profile

import android.os.Parcelable
import com.example.europeesaanrijdingsformulier.insurer.Insurer
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
import java.util.*

@Parcelize
class Insurance (@field:Json(name = "id")val id: Int,
                 @field:Json(name = "insuranceNumber")val insuranceNumber: String,
                 @field:Json(name = "greenCardNumber")val greenCardNumber: String,
                 @field:Json(name = "emailAgency")val emailAgency: String,
                 @field:Json(name = "expires")val expires: Date,
                 @field:Json(name = "phoneAgency")val phoneAgency: String,
                 @field:Json(name = "insurer")val insurer: Insurer? = null
                 ) : Parcelable, Serializable{


}