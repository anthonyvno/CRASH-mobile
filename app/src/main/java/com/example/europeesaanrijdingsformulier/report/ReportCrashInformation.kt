package com.example.europeesaanrijdingsformulier.report


import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import com.example.anthonyvannoppen.androidproject.utils.inReport
import com.example.europeesaanrijdingsformulier.MainActivity

import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.utils.DatePickerManager
import com.example.europeesaanrijdingsformulier.utils.SpinnerManager
import com.example.europeesaanrijdingsformulier.utils.Validator
import com.location.aravind.getlocation.GeoLocator
import kotlinx.android.synthetic.main.fragment_report_crash_information.*
import java.io.IOException
import java.util.*


class ReportCrashInformation : Fragment() {

    private lateinit var report: Report
    private lateinit var country: String
    private val spinnerManager = SpinnerManager()
    private val datePickerManager = DatePickerManager()
    private val validator = Validator()
    lateinit var geocoder: Geocoder
    private lateinit var geoLocator: GeoLocator


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bar = activity!! as AppCompatActivity
        bar.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        bar.supportActionBar!!.setDisplayShowHomeEnabled(true)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_crash_information, container, false)
    }

    override fun onStart() {
        super.onStart()
        geoLocator = GeoLocator(activity!!.applicationContext, activity)
        geocoder =  Geocoder(activity)

        AlertDialog.Builder(activity)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Huidige locatie gebruiken?")
            .setMessage("Bent u momenteel op de plaats van het ongeval?")
            .setPositiveButton(
                "Ja",
                DialogInterface.OnClickListener { dialog, which ->
                    getLocation()
                })

            .setNegativeButton("Nee", null)
            .show()





        datePickerManager.instantiateDatePicker(activity!!, R.id.textedit_report_crash_information_date)
        instantiateTimePicker()
        val optionCountries = spinnerManager.instantiateSpinner(
            activity!!,
            R.id.spinner_report_crash_information_country,
            getResources().getStringArray(R.array.countries_array)
        )

        optionCountries.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                country = optionCountries.adapter.getItem(position) as String
            }

        }

        button_report_crash_information_confirm.setOnClickListener {
            if(isValidated()){
                val date = textedit_report_crash_information_date.text.toString()
                val time = textedit_report_crash_information_time.text.toString()
                val dateSplit = date.split("/")
                val timeSplit = time.split(":")
                val dateReport = Date(
                    dateSplit[2].toInt() - 1900, dateSplit[1].toInt(), dateSplit[0].toInt()
                    , timeSplit[0].toInt(), timeSplit[1].toInt()
                )

                report = Report(
                    1, emptyList(), dateReport,
                    textedit_report_crash_information_street.text.toString(),
                    textedit_report_crash_information_streetNumber.text.toString(),
                    textedit_report_crash_information_postalCode.text.toString(),
                    textedit_report_crash_information_city.text.toString(),
                    country
                )


                val fragment = ReportStartAFragment()
                fragment.addObject(report)
                this.fragmentManager!!.beginTransaction()
                    .replace(R.id.container_main, fragment)
                     .addToBackStack(null)
                    .commit()
            }

        }
    }

    private fun getLocation() {
        var address = findGeocoder()
        textedit_report_crash_information_city.setText(address?.get(0)?.locality)
        //textedit_report_crash_information_city.setText(geoLocator.city)
        textedit_report_crash_information_street.setText(address?.get(0)?.thoroughfare)
        textedit_report_crash_information_streetNumber.setText(address?.get(0)?.subThoroughfare)
        textedit_report_crash_information_postalCode.setText(address?.get(0)?.postalCode)
    }

    private fun instantiateTimePicker() {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        val timepicker = activity!!.findViewById<EditText>(R.id.textedit_report_crash_information_time)

        timepicker.setOnClickListener() {
            val dpd = TimePickerDialog(activity, TimePickerDialog.OnTimeSetListener { view, hour, minute ->

                timepicker.setText("" + hour + ":" + minute)

            }, hour, minute, true)

            dpd.show()
        }

    }

    fun isValidated(): Boolean {
        if (validator.isNotEmpty(textedit_report_crash_information_date.text.toString())
            && validator.isNotEmpty(textedit_report_crash_information_time.text.toString())
            && validator.isNotEmpty(textedit_report_crash_information_city.text.toString())
            && validator.isNotEmpty(textedit_report_crash_information_postalCode.text.toString())
            && validator.isNotEmpty(textedit_report_crash_information_street.text.toString())
        ){
            return true
        } else{
            if(!validator.isNotEmpty(textedit_report_crash_information_date.text.toString())){
                textedit_report_crash_information_date.setError("Datum moet ingevuld zijn.")
            }
            if(!validator.isNotEmpty(textedit_report_crash_information_time.text.toString())){
                textedit_report_crash_information_time.setError("Tijdstip moet ingevuld zijn.")
            }
            if(!validator.isNotEmpty(textedit_report_crash_information_city.text.toString())){
                textedit_report_crash_information_city.setError("Stad moet ingevuld zijn.")
            }
            if(!validator.isNotEmpty(textedit_report_crash_information_postalCode.text.toString())){
                textedit_report_crash_information_postalCode.setError("Postcode moet ingevuld zijn.")
            }
            if(!validator.isNotEmpty(textedit_report_crash_information_street.text.toString())){
                textedit_report_crash_information_street.setError("Straat moet ingevuld zijn.")
            }
        }
        return false
    }

    private fun findGeocoder(): List<Address>? {
        val maxResults = 1
        println(geoLocator.lattitude)
        println(geoLocator.longitude)
        var addresses: List<Address>? = null
        try {
            addresses = geocoder.getFromLocation(geoLocator.lattitude, geoLocator.longitude, maxResults)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }

        return addresses
    }

}
