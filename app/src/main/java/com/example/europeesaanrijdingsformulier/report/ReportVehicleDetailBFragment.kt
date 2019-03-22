package com.example.europeesaanrijdingsformulier.report


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner

import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.profile.Vehicle
import kotlinx.android.synthetic.main.fragment_report_vehicle_detail_b.*


class ReportVehicleDetailBFragment : Fragment() {

    private lateinit var report: Report
    private var category: String = "Auto"
    private var country: String = "Belgium"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_vehicle_detail_b, container, false)
    }

    override fun onStart() {
        super.onStart()
        instantiateSpinners()
        if (report.profiles.last().vehicles?.first() != null) {
            fillInTextFields()
        }


        button_report_vehicle_detail_b_confirm.setOnClickListener{

            val brand = textedit_report_vehicle_detail_b_brand.text.toString()
            val model = textedit_report_vehicle_detail_b_model.text.toString()
            val licensePlate = textedit_report_vehicle_detail_b_licensePlate.text.toString()

            if(report.profiles.last().vehicles!!.isNotEmpty()){
                report.profiles.last().vehicles = listOf(Vehicle(1,country,licensePlate,brand,model,category,report.profiles.last().vehicles?.first()?.insurance))
            } else {
                report.profiles.last().vehicles = listOf(Vehicle(1,country,licensePlate,brand,model,category))
            }
            val fragment = ReportVehicleInsuranceBFragment()
            fragment.addObject(report)
            this.fragmentManager!!.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right)
                .replace(R.id.container_main, fragment)
                //.addToBackStack(null)
                .commit()
        }
    }

    private fun fillInTextFields() {
        textedit_report_vehicle_detail_b_brand.setText(report.profiles.last().vehicles?.first()?.brand)
        textedit_report_vehicle_detail_b_licensePlate.setText(report.profiles.last().vehicles?.first()?.licensePlate)
        textedit_report_vehicle_detail_b_model.setText(report.profiles.last().vehicles?.first()?.model)
    }



    fun addObject(item: Report) {
        this.report = item
    }

    private fun instantiateSpinners() {

        val option = activity!!.findViewById<Spinner>(R.id.spinner_report_vehicle_detail_b_type)
        val optionCountries = activity!!.findViewById<Spinner>(R.id.spinner_report_vehicle_detail_b_country)
        val adapter = ArrayAdapter(
            activity!!,
            android.R.layout.simple_spinner_item,
            getResources().getStringArray(R.array.vehicleCategory)
        )
        val adapterCountry = ArrayAdapter(
            activity!!,
            android.R.layout.simple_spinner_item,
            getResources().getStringArray(R.array.countries_array)
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapterCountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        option.adapter = adapter
        optionCountries.adapter = adapterCountry

        if (report.profiles.last().vehicles!!.isNotEmpty() && report.profiles.last().vehicles?.first()?.type != null) {
            val spinnerPosition = adapter.getPosition(report.profiles.last().vehicles?.first()!!.type)
            option.setSelection(spinnerPosition)
        }
        if (report.profiles.last().vehicles!!.isNotEmpty() && report.profiles.last().vehicles?.first()?.country != null) {
            val spinnerPosition = adapterCountry.getPosition(report.profiles.last().vehicles?.first()!!.country)
            optionCountries.setSelection(spinnerPosition)
        }

        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                category = adapter.getItem(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        };

        optionCountries.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                country = adapterCountry.getItem(position)
            }

        }


    }


}
