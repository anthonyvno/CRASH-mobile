package com.example.europeesaanrijdingsformulier.report


import android.app.DatePickerDialog
import android.app.TimePickerDialog
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

import com.example.europeesaanrijdingsformulier.R
import kotlinx.android.synthetic.main.fragment_report_crash_information.*
import java.util.*


class ReportCrashInformation : Fragment() {

    private lateinit var report: Report
    private lateinit var country: String



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
        instantiate()

        button_report_crash_information_confirm.setOnClickListener{
            val date = textedit_report_crash_information_date.text.toString()
            val time = textedit_report_crash_information_time.text.toString()
            val dateSplit = date.split("/")
            val timeSplit = time.split(":")
            val dateReport = Date(dateSplit[2].toInt()-1900,dateSplit[1].toInt(),dateSplit[0].toInt()
                ,timeSplit[0].toInt(),timeSplit[1].toInt())

            report = Report(1, emptyList(), dateReport,
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

    private fun instantiateDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datepicker = activity!!.findViewById<EditText>(R.id.textedit_report_crash_information_date)

        datepicker.setOnClickListener(){
            val dpd = DatePickerDialog(activity, R.style.MySpinnerDatePickerStyle, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                datepicker.setText("" + dayOfMonth + "/" + monthOfYear + "/" + year)

            }, year, month, day)

            dpd.show()
        }

    }

    private  fun instantiate(){
        instantiateDatePicker()
        instantiateTimePicker()
        instantiateSpinners()
    }

    private fun instantiateTimePicker() {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        val timepicker = activity!!.findViewById<EditText>(R.id.textedit_report_crash_information_time)

        timepicker.setOnClickListener(){
            val dpd = TimePickerDialog(activity, TimePickerDialog.OnTimeSetListener { view,  hour, minute ->

                timepicker.setText("" + hour + ":" + minute)

            }, hour, minute,true)

            dpd.show()
        }

    }

    private fun instantiateSpinners() {
        val optionCountries = activity!!.findViewById<Spinner>(R.id.spinner_report_crash_information_country)
        val adapterCountry = ArrayAdapter(activity!!, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.countries_array))
        adapterCountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        optionCountries.adapter = adapterCountry

        optionCountries.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                country = adapterCountry.getItem(position)
            }

        }

    }

    }