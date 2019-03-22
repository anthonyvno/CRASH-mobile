package com.example.europeesaanrijdingsformulier.report


import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner

import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.profile.License
import kotlinx.android.synthetic.main.fragment_report_license_b.*
import java.util.*


class ReportLicenseBFragment : Fragment() {

    private lateinit var report: Report
    private var category: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_license_b, container, false)
    }

    override fun onStart() {
        super.onStart()
        instantiateSpinners()
        instantiateDatePicker()
        if (report.profiles.last().license != null) {
            fillInTextFields()
        }
        button_report_license_b_confirm.setOnClickListener {

            val expires = textedit_report_license_b_licenseNumber.text.toString()
            val licenseNumber = textedit_report_license_b_licenseNumber.text.toString()

            report.profiles.last().license = License(1, category, licenseNumber, expires)

            val reportVehicleDetailBFragment = ReportVehicleDetailBFragment()
            this.fragmentManager!!.beginTransaction()
                .replace(R.id.container_main, reportVehicleDetailBFragment)
                //.addToBackStack(null)
                .commit()
            reportVehicleDetailBFragment.addObject(report)

        }
    }


    private fun fillInTextFields() {
        textedit_report_license_b_expires.setText(report.profiles.last().license?.expires)
        textedit_report_license_b_licenseNumber.setText(report.profiles.last().license?.licenseNumber)
    }

    private fun instantiateSpinners() {

        val option = activity!!.findViewById<Spinner>(R.id.spinner_report_license_b_category)
        val adapter = ArrayAdapter(
            activity!!,
            android.R.layout.simple_spinner_item,
            getResources().getStringArray(R.array.licenseCategory)
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        option.adapter = adapter
        if (report?.profiles.last().license?.category != null) {
            val spinnerPosition = adapter.getPosition(report?.profiles.last().license?.category)
            option.setSelection(spinnerPosition)
        }

        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                category = adapter.getItem(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

    private fun instantiateDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datepicker = activity!!.findViewById<EditText>(R.id.textedit_report_license_b_expires)

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

    fun addObject(item: Report) {
        this.report = item
    }


}
