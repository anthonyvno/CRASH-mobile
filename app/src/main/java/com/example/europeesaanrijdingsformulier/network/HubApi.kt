package com.example.europeesaanrijdingsformulier.network

import com.example.europeesaanrijdingsformulier.insurer.Insurer
import com.example.europeesaanrijdingsformulier.profile.Insurance
import com.example.europeesaanrijdingsformulier.profile.License
import com.example.europeesaanrijdingsformulier.profile.Profile
import com.example.europeesaanrijdingsformulier.profile.Vehicle
import com.example.europeesaanrijdingsformulier.report.Report
import io.reactivex.Observable
import retrofit2.http.*



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
    @POST("/insurances")
    fun addInsurance(@Body insurance: Insurance):Observable<Insurance>
    //@Headers("Content-Type: application/pdf")
    @POST("/reports/pdf")
    fun createPdf(@Body report: Report): Observable<Report>

    @PUT("/profiles/{id}")
    fun updateProfile(@Path("id") id: Int, @Body profile: Profile): Observable<Profile>
    @PUT("/licenses/{id}")
    fun updateLicense(@Path("id") id: Int, @Body license: License): Observable<License>
    @PUT("/vehicles/{id}")
    fun updateVehicle(@Path("id") id: Int, @Body vehicle: Vehicle): Observable<Vehicle>
    @PUT("/insurances/{id}")
    fun updateInsurance(@Path("id") id: Int, @Body insurance: Insurance): Observable<Insurance>

    @DELETE("vehicles/{id}")
    fun deleteVehicle(@Path("id") id: Int): Observable<Vehicle>

}