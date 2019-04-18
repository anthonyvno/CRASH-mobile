package com.example.europeesaanrijdingsformulier.utils

import android.util.Log
import com.example.europeesaanrijdingsformulier.profile.Insurance
import com.example.europeesaanrijdingsformulier.profile.Profile
import com.example.europeesaanrijdingsformulier.profile.Vehicle
import com.google.gson.Gson
import org.json.JSONObject
import java.util.*

class QRManager {

    val gson = Gson()

    fun handleGreenCardScan(scannedJson: String): Profile {
        val json = JSONObject(scannedJson)
        val insured = json.getJSONObject("insured")
        val insurance = json.getJSONObject("insurer")
        val vehicle = json.getJSONObject("vehicle")
        //Log.d("insured",insured.toString())
        val keys = insured.keys()
        val keys2 = insurance.keys()
        val keys3 = vehicle.keys()

        var firstname = ""
        var lastname = ""

        var contractnumber = ""
        var emailAgency = ""
        var expires = ""

        var brand = ""
        var licensePlate = ""
        var country = ""


        while (keys.hasNext()) {
            val key = keys.next() as String
            Log.d("keys", keys.next())
            firstname = insured.optString("firstname")

            lastname = insured.optString("lastname")

        }
        while (keys2.hasNext()) {
            keys2.next()
            emailAgency = insurance.optString("representativeEmail")
            contractnumber = insurance.optString("contractNumber")
            expires = insurance.optString("valid_until")
        }
        while (keys3.hasNext()) {
            keys3.next()
            brand = vehicle.optString("brand")
            licensePlate = vehicle.optString("licensePlate")
            country = vehicle.optString("country")
        }

        val dateSplit = expires.split("-")
        val dateExpires =
            Date(dateSplit[0].toInt() - 1900, dateSplit[1].toInt() - 1, dateSplit[2].substring(0, 2).toInt() + 1)

        return Profile(
            1, firstname, lastname, "", null,
            listOf(
                Vehicle(
                    1, country, licensePlate, brand, "", "",
                    Insurance(1, contractnumber, "", emailAgency, dateExpires, "")
                )
            )
        )

    }

    fun handleProfileScan(scannedJson: String): Profile {

        return gson.fromJson(scannedJson, Profile::class.java)

    }
}