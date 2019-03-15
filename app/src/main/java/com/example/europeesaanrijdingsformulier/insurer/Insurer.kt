package com.example.europeesaanrijdingsformulier.insurer

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
class  Insurer(@field:Json(name = "id")val id: Int,
              @field:Json(name = "name")val name: String,
              @field:Json(name = "country")val country: String) : Parcelable,Serializable {



}