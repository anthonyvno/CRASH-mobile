package com.example.europeesaanrijdingsformulier.profile


import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.anthonyvannoppen.androidproject.ui.HubViewModel

import com.example.europeesaanrijdingsformulier.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_profile_vehicle_detail.*


class ProfileVehicleDetailFragment : Fragment() {

    private lateinit var vehicle: Vehicle
    private lateinit var viewModel: HubViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(activity!!).get(HubViewModel::class.java)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_vehicle_detail, container, false)
    }

    override fun onStart() {
        super.onStart()

        val sharedPref = activity?.getSharedPreferences(R.string.preferences_profile.toString(), Context.MODE_PRIVATE)

        val gson: Gson = Gson()

        button_vehicle_detail_confirm.setOnClickListener{
            val brand = textedit_vehicle_detail_brand.text.toString()
            val model = textedit_vehicle_detail_model.text.toString()
            val type = textedit_vehicle_detail_type.text.toString()
            val country = textedit_vehicle_detail_country.text.toString()
            val licensePlate = textedit_vehicle_detail_licensePlate.text.toString()

            vehicle = Vehicle(1,country,licensePlate,brand,model,type)
            val vehicle2 = viewModel.postVehicle(vehicle).blockingFirst()


            val jsonAlles = sharedPref!!.getString("My_Vehicles","")
            val itemType = object : TypeToken<List<Vehicle>>() {}.type

            var vehicles = gson.fromJson<MutableList<Vehicle>>(jsonAlles, itemType)

            if(vehicles!=null){
                vehicles.add(vehicle2)
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
                .addToBackStack(null)
                .commit()

        }
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


}
