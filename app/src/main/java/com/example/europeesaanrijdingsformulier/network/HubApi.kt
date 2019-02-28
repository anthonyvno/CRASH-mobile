package com.example.anthonyvannoppen.androidproject.network

import com.example.europeesaanrijdingsformulier.insurer.Insurer
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface HubApi {


    @Headers("Content-Type: application/json;charset=utf-8")
    @GET("/insurers")
    fun getAllInsurers(): Observable<List<Insurer>>

    //@Headers("Content-Type: application/json;charset=utf-8")
    /*
    @FormUrlEncoded
    @POST("/api/Hubs")
    //fun addHub(@Body Hub: Hub): Observable<Hub>
    fun addHub(@Field("Op") op: String,
                @Field("Titel") titel: String,
                @Field("Categorie") categorie: String,
                @Field("Beschrijving") beschrijving: String,
                @Field("Afbeelding") afbeelding: String): Observable<Hub>

    @FormUrlEncoded
    @POST("/api/comments")
    fun addComment(@Field("Op") op: String,
                   @Field("Tekst") tekst: String,
                   @Field("HubID") id: Int): Observable<Comment>
                   */
    //fun addComment(@Body comment: Comment): Call<Comment>

}