package com.example.europeesaanrijdingsformulier.profile


import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.anthonyvannoppen.androidproject.ui.HubViewModel

import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.utils.PrefManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_profile_vehicle_detail.*


class ProfileVehicleDetailFragment : Fragment() {

    private var vehicle: Vehicle? = null
    private lateinit var viewModel: HubViewModel
    private lateinit var prefManager: PrefManager
    private var category: String = "Auto"
    private var country: String = "Belgium"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(activity!!).get(HubViewModel::class.java)
        prefManager = PrefManager(activity)

        return inflater.inflate(R.layout.fragment_profile_vehicle_detail, container, false)
    }

    override fun onStart() {
        super.onStart()
        instantiateSpinners()
        if (vehicle != null) {
            fillInTextFields()
        }


        button_vehicle_detail_confirm.setOnClickListener {
            val brand = textedit_vehicle_detail_brand.text.toString()
            val model = textedit_vehicle_detail_model.text.toString()
            val licensePlate = textedit_vehicle_detail_licensePlate.text.toString()
            if (vehicle == null) {
                vehicle = Vehicle(1, country, licensePlate, brand, model, category)
            } else {
                vehicle = Vehicle(vehicle!!.id, country, licensePlate, brand, model, category,vehicle!!.insurance)
            }


            val fragment = ProfileVehicleInsuranceFragment()
            fragment.addObject(vehicle!!)
            this.fragmentManager!!.beginTransaction()
                .replace(R.id.container_main, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun fillInTextFields() {
        textedit_vehicle_detail_brand.setText(vehicle!!.brand)
        textedit_vehicle_detail_licensePlate.setText(vehicle!!.licensePlate)
        textedit_vehicle_detail_model.setText(vehicle!!.model)
    }

    fun addObject(item: Vehicle) {
        this.vehicle = item
    }

    companion object {
        const val ARG_VEHICLE = "item_id"
        fun newInstance(veh: Vehicle): ProfileVehicleDetailFragment {
            val args = Bundle()
            args.putSerializable(ARG_VEHICLE, veh)
            val fragment = ProfileVehicleDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private fun instantiateSpinners() {

        val option = activity!!.findViewById<Spinner>(R.id.spinner_vehicle_detail_type)
        val optionCountries = activity!!.findViewById<Spinner>(R.id.spinner_vehicle_detail_country)
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

        if (vehicle?.type != null) {
            val spinnerPosition = adapter.getPosition(vehicle!!.type)
            option.setSelection(spinnerPosition)
        }
        if (vehicle?.country != null) {
            val spinnerPosition = adapterCountry.getPosition(vehicle!!.country)
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
