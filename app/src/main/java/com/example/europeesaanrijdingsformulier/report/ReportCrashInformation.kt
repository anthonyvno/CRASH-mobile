package com.example.europeesaanrijdingsformulier.report


import android.Manifest
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
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
    private lateinit var geocoder: Geocoder
    private lateinit var optionCountries: Spinner
    private lateinit var geoLocator: GeoLocator
     //  var translate = TranslateOptions.getDefaultInstance().getService();



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


        AlertDialog.Builder(activity)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Huidige locatie gebruiken?")
            .setMessage("Bent u momenteel op de plaats van het ongeval?")
            .setPositiveButton(
                "Ja"
            ) { dialog, which ->
                useLocation()

            }

            .setNegativeButton("Nee", null)
            .show()




        datePickerManager.instantiateDatePicker(activity!!, R.id.textedit_report_crash_information_date)
        instantiateTimePicker()
        optionCountries = spinnerManager.instantiateSpinner(
            activity!!,
            R.id.spinner_report_crash_information_country,
            resources.getStringArray(R.array.countries_array)
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
                    dateSplit[2].toInt() - 1900, dateSplit[1].toInt()-1, dateSplit[0].toInt()
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

    private fun useLocation() {
        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(

                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_ACCESS_FINE_LOCATION)
            return
        }
        try{
            geoLocator = GeoLocator(context, activity)
            geocoder =  Geocoder(activity)
            getLocation()
        }catch (e:Exception){
            println(e.message)
        }
    }

    private fun getLocation() {
        val address = findGeocoder()
        textedit_report_crash_information_city.setText(address?.get(0)?.locality)
        //textedit_report_crash_information_city.setText(geoLocator.city)
        textedit_report_crash_information_street.setText(address?.get(0)?.thoroughfare)
        textedit_report_crash_information_streetNumber.setText(address?.get(0)?.subThoroughfare)
        textedit_report_crash_information_postalCode.setText(address?.get(0)?.postalCode)
        textedit_report_crash_information_date.setText(Date().date.toString()+"/"+(Date().month+1).toString()+"/"+(Date().year+1900).toString())
        textedit_report_crash_information_time.setText(Date().hours.toString()+":"+Date().minutes.toString())

        println(address?.get(0)?.countryName)


        val adapter = optionCountries.adapter as ArrayAdapter<String>
        val spinnerPosition = adapter.getPosition(address?.get(0)?.countryName)
        optionCountries.setSelection(spinnerPosition)
    }

    private fun instantiateTimePicker() {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        val timepicker = activity!!.findViewById<EditText>(R.id.textedit_report_crash_information_time)

        timepicker.setOnClickListener {
            val dpd = TimePickerDialog(activity, TimePickerDialog.OnTimeSetListener { view, hour, minute ->

                timepicker.setText("$hour:$minute")

            }, hour, minute, true)

            dpd.show()
        }

    }

    private fun isValidated(): Boolean {
        if (validator.isNotEmpty(textedit_report_crash_information_date.text.toString())
            && validator.isNotEmpty(textedit_report_crash_information_time.text.toString())
            && validator.isNotEmpty(textedit_report_crash_information_city.text.toString())
            && validator.isNotEmpty(textedit_report_crash_information_streetNumber.text.toString())
            && validator.isNotEmpty(textedit_report_crash_information_street.text.toString())
            && validator.isNotEmpty(textedit_report_crash_information_postalCode.text.toString())
            && validator.isNotEmpty(textedit_report_crash_information_street.text.toString())
        ){
            return true
        } else{
            if(!validator.isNotEmpty(textedit_report_crash_information_date.text.toString())){
                textedit_report_crash_information_date.error = "Datum moet ingevuld zijn."
            }
            if(!validator.isNotEmpty(textedit_report_crash_information_time.text.toString())){
                textedit_report_crash_information_time.error = "Tijdstip moet ingevuld zijn."
            }
            if(!validator.isNotEmpty(textedit_report_crash_information_city.text.toString())){
                textedit_report_crash_information_city.error = "Stad moet ingevuld zijn."
            }
            if(!validator.isNotEmpty(textedit_report_crash_information_postalCode.text.toString())){
                textedit_report_crash_information_postalCode.error = "Postcode moet ingevuld zijn."
            }
            if(!validator.isNotEmpty(textedit_report_crash_information_street.text.toString())){
                textedit_report_crash_information_postalCode.error = "Postcode moet ingevuld zijn."
            }
            if(!validator.isNotEmpty(textedit_report_crash_information_streetNumber.text.toString())){
                textedit_report_crash_information_postalCode.error = "Postcode moet ingevuld zijn."
            }
            if(!validator.isNotEmpty(textedit_report_crash_information_street.text.toString())){
                textedit_report_crash_information_street.error = "Straat moet ingevuld zijn."
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
            //addresses = geocoder.getFromLocation(50.387565000000000, 4.459637000000000, maxResults)

        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }

        return addresses
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        println("permission result")

        if (requestCode == PERMISSION_REQUEST_ACCESS_FINE_LOCATION) {
            when (grantResults[0]) {
                PackageManager.PERMISSION_GRANTED -> {
                    try{
                        geoLocator = GeoLocator(context, activity)
                        geocoder =  Geocoder(activity)
                        getLocation()
                    }catch (e:Exception){
                        println(e.message)
                    }
                }
                PackageManager.PERMISSION_DENIED -> println("need perm")//Tell to user the need of grant permission
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 100
    }

}
