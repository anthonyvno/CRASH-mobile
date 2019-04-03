package com.example.europeesaanrijdingsformulier.utils

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
import java.util.*

@Parcelize
class PdfReport(@field:Json(name = "name")val name: String,
                @field:Json(name = "pdf")val pdf: String) : Parcelable, Serializable{

}