package com.example.europeesaanrijdingsformulier.ui

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.example.europeesaanrijdingsformulier.base.InjectedViewModel
import com.example.europeesaanrijdingsformulier.insurer.Insurer
import com.example.europeesaanrijdingsformulier.network.HubApi
import com.example.europeesaanrijdingsformulier.profile.Insurance
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
    private val loadingVisibility: MutableLiveData<Boolean> = MutableLiveData()

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

    fun getNogInsurers(): Observable<List<Insurer>>{
        return hubApi.getAllInsurers()
    }


    private fun postLicense(license:License):Observable<License>{
        val returnedLicense = hubApi.addLicense(license)
        Log.d("license tag",license.category+license.licenseNumber)

        return returnedLicense
    }

    private fun postVehicle(vehicle:Vehicle):Observable<Vehicle>{
        return hubApi.addVehicle(vehicle)
    }

    private fun postProfile(profile: Profile): Observable<Profile> {
        return hubApi.addProfile(profile)
    }

    fun postReport(report: Report){

        val profiles2: MutableList<Profile> = emptyList<Profile>().toMutableList()
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
        return hubApi.createPdf(report)
    }

    private fun postInsurance(insurance: Insurance): Observable<Insurance> {
        return hubApi.addInsurance(insurance)
    }

}