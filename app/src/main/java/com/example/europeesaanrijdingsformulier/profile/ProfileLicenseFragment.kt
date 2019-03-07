package com.example.europeesaanrijdingsformulier.profile


import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil.setContentView
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.anthonyvannoppen.androidproject.ui.HubViewModel

import com.example.europeesaanrijdingsformulier.R
import com.google.gson.Gson
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner
import kotlinx.android.synthetic.main.fragment_profile_license.*
import android.content.Intent









class ProfileLicenseFragment : Fragment() {

    private lateinit var license: License
    private lateinit var viewModel: HubViewModel
    private var category : String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(activity!!).get(HubViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_license, container, false)
    }

    override fun onStart() {
        super.onStart()
        instantiateSpinners()
        val sharedPref = activity?.getSharedPreferences(R.string.preferences_profile.toString(), Context.MODE_PRIVATE)

        val gson: Gson = Gson()

        button_profile_license_confirm.setOnClickListener{
            //val category = textedit_profile_license_category.text.toString()
            val expires = textedit_profile_license_expires.text.toString()
            val licenseNumber = textedit_profile_license_licenseNumber.text.toString()

            license = License(1,category,licenseNumber,expires)

            val license2 = viewModel.postLicense(license).blockingFirst()

            val json = gson.toJson(license2)

            with (sharedPref!!.edit()) {
                putString("My_License", json)
                commit()
            }
            val json2 = sharedPref!!.getString("My_Profile","")
            val profile = gson.fromJson(json2,Profile::class.java)
            profile.license = license2
            val newProfile = gson.toJson(profile)
            with (sharedPref!!.edit()) {
                putString("My_Profile", newProfile)
                commit()
            }

            this.fragmentManager!!.beginTransaction()
                .replace(com.example.europeesaanrijdingsformulier.R.id.container_main, ProfileSummaryFragment())
                //.addToBackStack(null)
                .commit()
        }
    }

    private fun instantiateSpinners(){

        val option = activity!!.findViewById<Spinner>(R.id.spinner_profile_license_category)
        val adapter = ArrayAdapter(activity!!, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.licenseCategory))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        option.setAdapter(adapter)

        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                category = adapter.getItem(position)
                Log.d("testpurp","item selected")
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Log.d("testpurp","no item selected")
            }
        };

    }


}
