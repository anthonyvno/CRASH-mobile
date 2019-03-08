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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_profile_vehicle_detail.*


class ProfileVehicleDetailFragment : Fragment() {

    private var vehicle: Vehicle? = null
    private lateinit var viewModel: HubViewModel
    private var category : String = "Auto"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(activity!!).get(HubViewModel::class.java)

        return inflater.inflate(R.layout.fragment_profile_vehicle_detail, container, false)
    }

    override fun onStart() {
        super.onStart()
        instantiateSpinners()
        if(vehicle != null){
            fillInTextFields()
        }
        val sharedPref = activity?.getSharedPreferences(R.string.preferences_profile.toString(), Context.MODE_PRIVATE)

        val gson: Gson = Gson()

        button_vehicle_detail_confirm.setOnClickListener{
            val brand = textedit_vehicle_detail_brand.text.toString()
            val model = textedit_vehicle_detail_model.text.toString()
            val country = textedit_vehicle_detail_country.text.toString()
            val licensePlate = textedit_vehicle_detail_licensePlate.text.toString()
            val vehicle2 : Vehicle
            if(vehicle == null){
                vehicle = Vehicle(1,country,licensePlate,brand,model,category)
                 vehicle2 = viewModel.postVehicle(vehicle!!).blockingFirst()
            }else{
                vehicle = Vehicle(vehicle!!.id, country, licensePlate, brand, model, category)
                vehicle2 = viewModel.updateVehicle(vehicle!!).blockingFirst()
            }


            val jsonAlles = sharedPref!!.getString("My_Vehicles","")
            val itemType = object : TypeToken<List<Vehicle>>() {}.type

            var vehicles = gson.fromJson<MutableList<Vehicle>>(jsonAlles, itemType)

            if(vehicles!=null){
               var comparevehicle =  vehicles.find { it.id == vehicle2.id}
                if(comparevehicle == null){
                    vehicles.add(vehicle2)
                }else{
                    vehicles.remove(comparevehicle)
                    vehicles.add(vehicle2)
                }
            } else {
                vehicles = mutableListOf<Vehicle>(vehicle2)
            }

            val json = gson.toJson(vehicles)

            with (sharedPref!!.edit()) {
                putString("My_Vehicles", json)
                commit()
            }
            this.fragmentManager!!.beginTransaction()
                .replace(R.id.container_main, ProfileSummaryFragment())
                //.addToBackStack(null)
                .commit()
        }
    }

    private fun fillInTextFields() {
        textedit_vehicle_detail_brand.setText(vehicle!!.brand)
        textedit_vehicle_detail_country.setText(vehicle!!.country)
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

    private fun instantiateSpinners(){

        val option = activity!!.findViewById<Spinner>(R.id.spinner_vehicle_detail_type)
        val adapter = ArrayAdapter(activity!!, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.vehicleCategory))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        option.adapter = adapter

        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                category = adapter.getItem(position)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        };


}
}
