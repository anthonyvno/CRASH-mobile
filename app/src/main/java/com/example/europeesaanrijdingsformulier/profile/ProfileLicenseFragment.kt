package com.example.europeesaanrijdingsformulier.profile


import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.anthonyvannoppen.androidproject.ui.HubViewModel

import com.example.europeesaanrijdingsformulier.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_profile_license.*


class ProfileLicenseFragment : Fragment() {

    private lateinit var license: License
    private lateinit var viewModel: HubViewModel

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

        val sharedPref = activity?.getSharedPreferences(R.string.preferences_profile.toString(), Context.MODE_PRIVATE)

        val gson: Gson = Gson()

        button_profile_license_confirm.setOnClickListener{
            val category = textedit_profile_license_category.text.toString()
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
                .replace(R.id.container_main, ProfileSummaryFragment())
                .addToBackStack(null)
                .commit()
        }
    }


}
