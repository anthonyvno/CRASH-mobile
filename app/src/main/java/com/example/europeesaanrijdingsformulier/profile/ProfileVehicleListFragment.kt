package com.example.europeesaanrijdingsformulier.profile


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.utils.PrefManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_profile_vehicle_list.*


class ProfileVehicleListFragment : Fragment() {

    private lateinit var adapter: VehicleViewAdapter
    private lateinit var prefManager: PrefManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prefManager = PrefManager(activity)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_vehicle_list, container, false)
    }

    override fun onStart() {
        super.onStart()



        val vehicles = prefManager.getVehicles()
        if(vehicles!=null){
            adapter =VehicleViewAdapter(this, vehicles)
            fragment_profile_vehicle_list.adapter = adapter
        } else {
            adapter = VehicleViewAdapter(this, emptyList())
            fragment_profile_vehicle_list.adapter = adapter

        }

        button_vehicle_list_add.setOnClickListener{
            this.fragmentManager!!.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right)
                .replace(R.id.container_main,ProfileVehicleDetailFragment(),"detail")
                .addToBackStack("list_to_detail")
                .commit()
        }


    }

    fun startNewFragmentForDetail(item: Vehicle) {
        val profileVehicleDetailFragment = ProfileVehicleDetailFragment()
        profileVehicleDetailFragment.addObject(item)
        this.fragmentManager!!.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right)
            .replace(R.id.container_main,profileVehicleDetailFragment,"detail")
            .addToBackStack("list_to_detail")
            .commit()

    }


}
