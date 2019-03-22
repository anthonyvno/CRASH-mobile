package com.example.europeesaanrijdingsformulier.report


import android.app.DatePickerDialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import com.example.anthonyvannoppen.androidproject.ui.HubViewModel

import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.insurer.Insurer
import com.example.europeesaanrijdingsformulier.profile.Insurance
import kotlinx.android.synthetic.main.fragment_report_vehicle_insurance_a.*
import java.util.*

class ReportVehicleInsuranceAFragment : Fragment() {

    private lateinit var report: Report
    private lateinit var insurerName: String
    private lateinit var viewModel: HubViewModel
    private lateinit var insurers: List<Insurer?>




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(activity!!).get(HubViewModel::class.java)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_vehicle_insurance_a, container, false)
    }

    override fun onStart() {
        super.onStart()
        instantiateSpinners()
        instantiateDatePicker()
        if (report.profiles.first().vehicles?.first()?.insurance != null) {
            fillInTextFields()
        }
        button_report_vehicle_insurance_a_confirm.setOnClickListener{
            val date = textedit_report_vehicle_insurance_a_expires.text.toString()
            val dateSplit = date.split("/")
            val dateExpires = Date(
                dateSplit[2].toInt() - 1900, dateSplit[1].toInt() -1, dateSplit[0].toInt() +1
            )
            val insurer4 = insurers.find { insurer -> insurer!!.name == insurerName }
            var insurance = Insurance(
                1,
                textedit_report_vehicle_insurance_a_insuranceNumber.text.toString(),
                textedit_report_vehicle_insurance_a_greenCard.text.toString(),
                textedit_report_vehicle_insurance_a_email.text.toString(),
                dateExpires,
                textedit_report_vehicle_insurance_a_phone.text.toString(),
                insurer4
            )

            report.profiles.first().vehicles?.first()?.insurance = insurance

            val fragment = ReportStartBFragment()
            fragment.addObject(report)
            this.fragmentManager!!.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right)
                .replace(R.id.container_main, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun instantiateSpinners() {
        insurers = viewModel.getInsurers().value!!
        Log.d("testpurp2", insurers.toString())
        val option = activity!!.findViewById<Spinner>(R.id.spinner_report_vehicle_insurance_a_insurer)
        val adapter =
            ArrayAdapter(activity!!, android.R.layout.simple_spinner_item, insurers.map { insurer -> insurer!!.name })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        option.adapter = adapter

        if (report.profiles.first().vehicles?.first()?.insurance?.insurer != null) {
            val spinnerPosition = adapter.getPosition(report.profiles.first().vehicles?.first()?.insurance!!.insurer!!.name)
            option.setSelection(spinnerPosition)
        }

        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                insurerName = adapter.getItem(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        };
    }

    private fun instantiateDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datepicker = activity!!.findViewById<EditText>(R.id.textedit_report_vehicle_insurance_a_expires)

        datepicker.setOnClickListener() {
            val dpd = DatePickerDialog(
                activity,
                R.style.MySpinnerDatePickerStyle,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    var monthOfYear2 = monthOfYear +1
                    datepicker.setText("" + dayOfMonth + "/" + monthOfYear2 + "/" + year)
                },
                year,
                month,
                day
            )

            dpd.show()
        }

    }

    private fun fillInTextFields() {
        textedit_report_vehicle_insurance_a_email.setText(report.profiles.first().vehicles?.first()?.insurance!!.emailAgency)
        textedit_report_vehicle_insurance_a_phone.setText(report.profiles.first().vehicles?.first()?.insurance!!.phoneAgency)
        textedit_report_vehicle_insurance_a_greenCard.setText(report.profiles.first().vehicles?.first()?.insurance!!.greenCardNumber)
        textedit_report_vehicle_insurance_a_insuranceNumber.setText(report.profiles.first().vehicles?.first()?.insurance!!.insuranceNumber)
        if(report.profiles.first().vehicles?.first()?.insurance!!.expires != null){
            val expiresYear = report.profiles.first().vehicles?.first()?.insurance!!.expires!!.year + 1900
            val expiresMonth = report.profiles.first().vehicles?.first()?.insurance!!.expires!!.month + 1
            val expiresDate = report.profiles.first().vehicles?.first()?.insurance!!.expires!!.date - 1
            val expiresvalue = (""+expiresDate.toString() + "/" +
                    expiresMonth.toString() + "/" + expiresYear.toString() )
            textedit_report_vehicle_insurance_a_expires.setText(expiresvalue)
        }

    }
    fun addObject(item: Report) {
        this.report = item
    }
}
