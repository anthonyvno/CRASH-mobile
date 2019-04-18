package com.example.europeesaanrijdingsformulier.report


import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.anthonyvannoppen.androidproject.ui.HubViewModel

import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.fragments.HomeFragment
import com.example.europeesaanrijdingsformulier.profile.License
import com.example.europeesaanrijdingsformulier.profile.Profile
import com.example.europeesaanrijdingsformulier.utils.Validator
import kotlinx.android.synthetic.main.fragment_report_algemeen_b.*


class ReportAlgemeenBFragment : Fragment() {

    private lateinit var report: Report
    private var profile: Profile? = null
    private val validator = Validator()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_report_algemeen_b, container, false)
    }

    override fun onStart() {
        super.onStart()

        if (profile != null) {
            textedit_algemeen_b_firstname.setText(profile!!.firstName)
            textedit_algemeen_b_lastname.setText(profile!!.lastName)
            textedit_algemeen_b_email.setText(profile!!.email)

        }

        button_algemeen_b_confirm.setOnClickListener {
            if (isValidated()) {
                val firstName = this.textedit_algemeen_b_firstname.text.toString()
                val lastName = this.textedit_algemeen_b_lastname.text.toString()
                val email = this.textedit_algemeen_b_email.text.toString()


                var profiles: List<Profile>
                if (profile != null) {
                    profiles = listOf(
                        this.report.profiles.first(),
                        Profile(1, firstName, lastName, email, profile?.license, profile?.vehicles)
                    )
                } else {
                    profiles = listOf(this.report.profiles.first(), Profile(1, firstName, lastName, email))
                }

                report.profiles = profiles

                val reportLicenseBFragment = ReportLicenseBFragment()
                reportLicenseBFragment.addObject(report)
                this.fragmentManager!!.beginTransaction()
                    .setCustomAnimations(
                        R.anim.enter_from_right,
                        R.anim.exit_to_left,
                        R.anim.enter_from_left,
                        R.anim.exit_to_right
                    )
                    .replace(R.id.container_main, reportLicenseBFragment)
                    .addToBackStack(null)
                    .commit()
            }

        }
    }

    fun addReport(item: Report) {
        this.report = item
    }

    fun addProfile(item: Profile) {
        this.profile = item
    }

    fun isValidated(): Boolean {
        if (validator.isNotEmpty(this.textedit_algemeen_b_firstname.text.toString()) ||
            validator.isNotEmpty(this.textedit_algemeen_b_lastname.text.toString())
        ) {
            if (validator.isValidEmail(this.textedit_algemeen_b_email.text.toString())) {
                return true
            } else {
                this.textedit_algemeen_b_email.setError("Geen geldig e-mail adres")
            }
        } else Toast.makeText(
            activity,
            Html.fromHtml("<font color='#FF0000' ><b>" + "Voornaam of achternaam moet ingevuld zijn." + "</b></font>"),
            Toast.LENGTH_LONG
        ).show()

        return false
    }


}
