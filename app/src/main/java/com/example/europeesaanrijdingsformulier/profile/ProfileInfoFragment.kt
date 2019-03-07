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
import kotlinx.android.synthetic.main.fragment_profile_info.*

class ProfileInfoFragment : Fragment() {

    private lateinit var profile: Profile
    private lateinit var viewModel: HubViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(activity!!).get(HubViewModel::class.java)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_info, container, false)
    }

    override fun onStart() {
        super.onStart()

        val sharedPref = activity?.getSharedPreferences(R.string.preferences_profile.toString(), Context.MODE_PRIVATE)

        val gson: Gson = Gson()

        button_profile_info_confirm.setOnClickListener{
            val firstName =  this.textedit_profile_info_firstname.text.toString()
            val lastName = this.textedit_profile_info_lastname.text.toString()
            val email = this.textedit_profile_info_email.text.toString()

            profile = Profile(1,firstName,lastName,email)


            //Log.d("sanderiseentag",profile.email+profile.firstName+profile.lastName)
            val profile2 = viewModel.postProfile(profile).blockingFirst()

            val json = gson.toJson(profile2)

            with (sharedPref!!.edit()) {
                putString("My_Profile", json)
                commit()
            }

            this.fragmentManager!!.beginTransaction()
                .replace(R.id.container_main, ProfileSummaryFragment())
                .addToBackStack(null)
                .commit()


        }
    }


}
