package com.example.europeesaanrijdingsformulier.profile


import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import com.example.anthonyvannoppen.androidproject.ui.HubViewModel

import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.utils.PrefManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_profile_info.*

class ProfileInfoFragment : Fragment() {

    private lateinit var profile: Profile
    private var profileInput: Profile? = null
    private lateinit var prefManager: PrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prefManager = PrefManager(activity)

        return inflater.inflate(R.layout.fragment_profile_info, container, false)
    }

    override fun onStart() {
        super.onStart()


        profileInput = prefManager.getProfile()
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
                Log.d("testpurp","1")
            } else{
                profile = Profile(profileInput!!.id,firstName,lastName,email)
                Log.d("testpurp","2")
            }

            prefManager.saveProfile(profile)

            this.fragmentManager!!.beginTransaction()
                .setCustomAnimations(R.anim.abc_fade_in,R.anim.abc_fade_out,R.anim.abc_fade_in,R.anim.abc_fade_out)
                .replace(R.id.container_main, ProfileSummaryFragment(),"summary")
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
