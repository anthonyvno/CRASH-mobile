package com.example.anthonyvannoppen.androidproject.ui

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.example.anthonyvannoppen.androidproject.base.InjectedViewModel

import com.example.anthonyvannoppen.androidproject.network.HubApi
import com.example.europeesaanrijdingsformulier.profile.Insurance
import com.example.europeesaanrijdingsformulier.insurer.Insurer
import com.example.europeesaanrijdingsformulier.profile.License
import com.example.europeesaanrijdingsformulier.profile.Profile
import com.example.europeesaanrijdingsformulier.profile.Vehicle
import com.example.europeesaanrijdingsformulier.report.Report
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HubViewModel: InjectedViewModel(){
    private val insurers = MutableLiveData<List<Insurer>>()
    @SuppressLint("NewApi")

    @Inject
    lateinit var hubApi: HubApi

    /**
     * Indicates whether the loading view should be displayed.
     */
    val loadingVisibility: MutableLiveData<Boolean> = MutableLiveData()

    /**
     * Represents a disposable resources
     */
    private var subscription: Disposable
    init {
        subscription = hubApi.getAllInsurers()
            //we tell it to fetch the data on background by
            .subscribeOn(Schedulers.io())
            //we like the fetched data to be displayed on the MainTread (UI)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRetrieveInsurerStart() }
            .doOnTerminate { onRetrieveInsurerFinish() }
            .subscribe(
                { result -> onRetrieveInsurerSuccess(result) },
                { error -> onRetrieveInsurerError(error) }
            )

    }

    private fun onRetrieveInsurerError(error: Throwable) {
        //Currently requests fail silently, which isn't great for the user.
        //It would be better to show a Toast, or maybe make a TextView visible with the error message.
        Logger.e(error.message!!)
    }

    @SuppressLint("NewApi")
    private fun onRetrieveInsurerSuccess(result: List<Insurer>) {
        insurers.value = result
    }

    private fun onRetrieveInsurerFinish() {
        Logger.i("Finished retrieving insurer info")
        loadingVisibility.value = false
    }

    private fun onRetrieveInsurerStart() {
        Logger.i("Started retrieving insurer info")
        loadingVisibility.value = true
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

    fun getInsurers(): MutableLiveData<List<Insurer>> {
        Log.d("robert", insurers.toString())
        return insurers
    }


    fun postLicense(license:License):Observable<License>{
        val returnedLicense = hubApi.addLicense(license)
        Log.d("license tag",license.category+license.licenseNumber)

        return returnedLicense
    }

    fun postVehicle(vehicle:Vehicle):Observable<Vehicle>{
        val returnedVehicle = hubApi.addVehicle(vehicle)
        return returnedVehicle
    }
    fun deleteVehicle(vehicle:Vehicle):Observable<Vehicle>{
        val returnedVehicle = hubApi.deleteVehicle(vehicle.id)
        return returnedVehicle
    }

    fun postProfile(profile: Profile): Observable<Profile> {
        val returnedProfiel  = hubApi.addProfile(profile)
        return returnedProfiel
    }

    fun postReport(report: Report){

        var profiles2: MutableList<Profile> = emptyList<Profile>().toMutableList()
        //var report2 = Report(1, emptyList(),report.dateCrash,report.street,report.streetNumber,report.postalCode,report.city,report.country)
        for (profile in report.profiles) {
            profile.vehicles!!.first().insurance = postInsurance(profile.vehicles!!.first().insurance!!).blockingFirst()
            profile.vehicles = listOf(postVehicle(profile.vehicles!!.first()).blockingFirst())
            profile.license = postLicense(profile.license!!).blockingFirst()
            profiles2.add(postProfile(profile).blockingFirst())

        }
        report.profiles = profiles2

        hubApi.addReport(report).
            subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> Log.v("POSTED ARTICLE", "" + report ) },
                { error -> Log.e("ERROR", error.message ) })
    }
    fun createPdf(report: Report):Observable<Report>{
        val pdf  = hubApi.createPdf(report)
        return pdf
    }

    fun updateProfile(profile: Profile): Observable<Profile> {
        val returnedProfiel  = hubApi.updateProfile(profile.id,profile)
        return returnedProfiel
    }

    fun updateLicense(license: License): Observable<License> {
        val returnedLicense  = hubApi.updateLicense(license.id,license)
        return returnedLicense
    }

    fun updateVehicle(vehicle: Vehicle): Observable<Vehicle> {
        val returnedVehicle  = hubApi.updateVehicle(vehicle.id,vehicle)
        return returnedVehicle
    }

    fun postInsurance(insurance: Insurance): Observable<Insurance> {
        val returnedInsurance  = hubApi.addInsurance(insurance)
        return returnedInsurance
    }

    fun updateInsurance(insurance: Insurance): Observable<Insurance> {
        val returnedInsurance  = hubApi.updateInsurance(insurance.id,insurance)
        return returnedInsurance
    }

}