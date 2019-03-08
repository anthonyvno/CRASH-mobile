package com.example.europeesaanrijdingsformulier.profile


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.europeesaanrijdingsformulier.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_profile_summary.*


class ProfileSummaryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val bar = activity!! as AppCompatActivity
        bar.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        bar.supportActionBar!!.setDisplayShowHomeEnabled(true)

        return inflater.inflate(R.layout.fragment_profile_summary, container, false)
    }

    override fun onStart() {
        super.onStart()

        val sharedPref = activity?.getSharedPreferences(R.string.preferences_profile.toString(), Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPref!!.getString("My_Profile","")
        val json2 = sharedPref!!.getString("My_License","")
        val profile = gson.fromJson(json,Profile::class.java)
        val license = gson.fromJson(json2,License::class.java)
        if(profile != null){
            text_profile_summary_personal.text = profile?.firstName+" "+profile?.lastName+profile?.id
        }
        else{
            cardview2_profile_summary.visibility = View.INVISIBLE
            cardview3_profile_summary.visibility = View.INVISIBLE
        }

        if(license != null)
            text_profile_summary_license.text = license?.category+" "+license?.licenseNumber+license?.id


        cardview1_profile_summary.setOnClickListener{

            this.fragmentManager!!.beginTransaction()
                .replace(R.id.container_main, ProfileInfoFragment())
                .addToBackStack(null)
                .commit()
        }

        cardview2_profile_summary.setOnClickListener{

            this.fragmentManager!!.beginTransaction()
                .replace(R.id.container_main, ProfileLicenseFragment())
                .addToBackStack(null)
                .commit()
        }

        cardview3_profile_summary.setOnClickListener{
            this.fragmentManager!!.beginTransaction()
                .replace(R.id.container_main, ProfileVehicleListFragment())
                .addToBackStack(null)
                .commit()
        }
    }




}
