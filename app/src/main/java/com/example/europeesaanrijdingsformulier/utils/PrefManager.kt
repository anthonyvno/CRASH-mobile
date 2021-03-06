package com.example.europeesaanrijdingsformulier.utils

import android.content.Context
import android.support.v4.app.FragmentActivity
import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.insurer.Insurer
import com.example.europeesaanrijdingsformulier.profile.License
import com.example.europeesaanrijdingsformulier.profile.Profile
import com.example.europeesaanrijdingsformulier.profile.Vehicle
import com.example.europeesaanrijdingsformulier.report.Report
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PrefManager(val parent: FragmentActivity?) {


    private val sharedPref = parent!!.getSharedPreferences(R.string.preferences_profile.toString(), Context.MODE_PRIVATE)!!
    private val gson = Gson()


    fun getProfile(): Profile? {
        val json = sharedPref.getString("My_Profile", "")
        return gson.fromJson(json, Profile::class.java)
    }

    fun saveProfile(profile: Profile) {
        val json = gson.toJson(profile)

        with(sharedPref.edit()) {
            putString("My_Profile", json)
            commit()
        }

    }

    fun getInsurers(): MutableList<Insurer>? {
        val json = sharedPref.getString("Insurers", "")
        val itemType = object : TypeToken<List<Insurer>>() {}.type

        return gson.fromJson<MutableList<Insurer>>(json, itemType)
    }
    fun saveInsurers(insurers: MutableList<Insurer>){
        val json = gson.toJson(insurers)

        with(sharedPref.edit()) {
            putString("Insurers", json)
            commit()
        }
    }

    fun getReport(): Report? {
        val json = sharedPref.getString("Report", "")
        return gson.fromJson(json, Report::class.java)
    }

    fun saveReport(report: Report) {
        val json = gson.toJson(report)

        with(sharedPref.edit()) {
            putString("Report", json)
            commit()
        }

    }

    fun getLicense(): License? {
        val json = sharedPref.getString("My_License", "")
        return gson.fromJson(json, License::class.java)
    }

    fun saveLicense(license: License) {
        val json = gson.toJson(license)

        with(sharedPref.edit()) {
            putString("My_License", json)
            commit()
        }

    }

    fun getVehicles(): MutableList<Vehicle>? {
        val json = sharedPref.getString("My_Vehicles", "")
        val itemType = object : TypeToken<List<Vehicle>>() {}.type

        return gson.fromJson<MutableList<Vehicle>>(json, itemType)
    }

    fun saveVehicles(vehicles: MutableList<Vehicle>) {
        val json = gson.toJson(vehicles)

        with(sharedPref.edit()) {
            putString("My_Vehicles", json)
            commit()
        }
    }

}