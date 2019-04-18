package com.example.europeesaanrijdingsformulier.profile


import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.Toast
import com.example.anthonyvannoppen.androidproject.ui.HubViewModel

import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.utils.PrefManager
import com.example.europeesaanrijdingsformulier.utils.Validator
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_profile_info.*

class ProfileInfoFragment : Fragment() {

    private lateinit var profile: Profile
    private var profileInput: Profile? = null
    private lateinit var prefManager: PrefManager
    private val validator = Validator()

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
            if(isValidated()){
                val firstName =  this.textedit_profile_info_firstname.text.toString()
                val lastName = this.textedit_profile_info_lastname.text.toString()
                val email = this.textedit_profile_info_email.text.toString()

                if(profileInput == null) {
                    profile = Profile(1,firstName,lastName,email)
                    Log.d("testpurp","1")
                } else{
                    profile = Profile(profileInput!!.id,firstName,lastName,email,profileInput!!.license,profileInput!!.vehicles)
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
    }

    private fun fillInTextFields() {
        textedit_profile_info_firstname.setText(profileInput!!.firstName)
        textedit_profile_info_lastname.setText(profileInput!!.lastName)
        textedit_profile_info_email.setText(profileInput!!.email)
    }
    fun isValidated():Boolean{
        if(validator.isNotEmpty(this.textedit_profile_info_firstname.text.toString()) ||
            validator.isNotEmpty(this.textedit_profile_info_lastname.text.toString())){
            if(validator.isValidEmail(this.textedit_profile_info_email.text.toString())){
                return true
            } else{
                this.textedit_profile_info_email.setError("Geen geldig e-mail adres")
            }
        } else Toast.makeText(
            activity,
            Html.fromHtml("<font color='#FF0000' ><b>" + "Voornaam of achternaam moet ingevuld zijn." + "</b></font>"),
            Toast.LENGTH_LONG
        ).show()

        return false



    }


}
