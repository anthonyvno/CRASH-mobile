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
    private var profileInput: Profile? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(activity!!).get(HubViewModel::class.java)

        return inflater.inflate(R.layout.fragment_profile_info, container, false)
    }

    override fun onStart() {
        super.onStart()

        val sharedPref = activity?.getSharedPreferences(R.string.preferences_profile.toString(), Context.MODE_PRIVATE)

        val gson = Gson()
        val json = sharedPref!!.getString("My_Profile","")
        profileInput = gson.fromJson(json,Profile::class.java)
        if(profileInput != null){
            fillInTextFields()
        }


        button_profile_info_confirm.setOnClickListener{
            val firstName =  this.textedit_profile_info_firstname.text.toString()
            val lastName = this.textedit_profile_info_lastname.text.toString()
            val email = this.textedit_profile_info_email.text.toString()

            val profile2: Profile
            if(profileInput == null) {
                profile = Profile(1,firstName,lastName,email)
                profile2 = viewModel.postProfile(profile).blockingFirst()
                Log.d("testpurp","1")
            } else{
                profile = Profile(profileInput!!.id,firstName,lastName,email)
                profile2 = viewModel.updateProfile(profile).blockingFirst()
                Log.d("testpurp","2")
            }

            val json = gson.toJson(profile2)

            with (sharedPref!!.edit()) {
                putString("My_Profile", json)
                commit()
            }

            this.fragmentManager!!.beginTransaction()
                .replace(R.id.container_main, ProfileSummaryFragment())
               // .addToBackStack(null)
                .commit()


        }
    }

    private fun fillInTextFields() {
        textedit_profile_info_firstname.setText(profileInput!!.firstName)
        textedit_profile_info_lastname.setText(profileInput!!.lastName)
        textedit_profile_info_email.setText(profileInput!!.email)
    }


}
