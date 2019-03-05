package com.example.europeesaanrijdingsformulier.profile


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.europeesaanrijdingsformulier.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_profile_vehicle_list.*


class ProfileVehicleListFragment : Fragment() {

    private lateinit var adapter: VehicleViewAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_vehicle_list, container, false)
    }

    override fun onStart() {
        super.onStart()

        val sharedPref = activity?.getSharedPreferences(R.string.preferences_profile.toString(), Context.MODE_PRIVATE)

        val gson = Gson()
        val json = sharedPref!!.getString("My_Vehicles","")
        Log.d("lokale autos",json)
        val itemType = object : TypeToken<List<Vehicle>>() {}.type

        val vehicles = gson.fromJson<List<Vehicle>>(json, itemType)
        Log.d("from json op lok",vehicles.first().brand)
        if(vehicles!=null){
            adapter =VehicleViewAdapter(this, vehicles)
            fragment_profile_vehicle_list.adapter = adapter
        } else {
            adapter = VehicleViewAdapter(this, emptyList())
            fragment_profile_vehicle_list.adapter = adapter

        }

        button_vehicle_list_add.setOnClickListener{
            this.fragmentManager!!.beginTransaction()
                .replace(R.id.container_main,ProfileVehicleDetailFragment())
                .addToBackStack(null)
                .commit()
        }


    }

    fun startNewFragmentForDetail(item: Vehicle) {
        val profileVehicleDetailFragment = ProfileVehicleDetailFragment()
        this.fragmentManager!!.beginTransaction()
            .replace(R.id.container_main,profileVehicleDetailFragment)
            .addToBackStack(null)
            .commit()
        profileVehicleDetailFragment.addObject(item)
    }


}
