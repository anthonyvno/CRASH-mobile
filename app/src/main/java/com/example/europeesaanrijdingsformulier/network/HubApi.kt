package com.example.anthonyvannoppen.androidproject.network

import com.example.europeesaanrijdingsformulier.insurer.Insurer
import com.example.europeesaanrijdingsformulier.profile.License
import com.example.europeesaanrijdingsformulier.profile.Profile
import com.example.europeesaanrijdingsformulier.profile.Vehicle
import com.example.europeesaanrijdingsformulier.report.Report
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import java.time.LocalDate
import java.util.*

interface HubApi {


    @Headers("Content-Type: application/json;charset=utf-8")
    @GET("/insurers")
    fun getAllInsurers(): Observable<List<Insurer>>

    @POST("/reports")
    fun addReport(@Body report: Report): Observable<Report>
    @POST("/profiles")
    fun addProfile(@Body profile: Profile):Observable<Profile>
    @POST("/licenses")
    fun addLicense(@Body license: License):Observable<License>
    @POST("/vehicles")
    fun addVehicle(@Body vehicle: Vehicle):Observable<Vehicle>

    @PUT("/reports/{id}")
    fun updateReport(@Path("id") id: Int, @Body report: Report): Observable<Report>
    @PUT("/profiles/{id}")
    fun updateProfile(@Path("id") id: Int, @Body profile: Profile): Observable<Profile>
    @PUT("/licenses/{id}")
    fun updateLicense(@Path("id") id: Int, @Body license: License): Observable<License>
    @PUT("/vehicles/{id}")
    fun updateVehicle(@Path("id") id: Int, @Body vehicle: Vehicle): Observable<Vehicle>
    
}