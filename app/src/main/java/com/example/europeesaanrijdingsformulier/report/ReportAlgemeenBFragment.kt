package com.example.europeesaanrijdingsformulier.report


import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.anthonyvannoppen.androidproject.ui.HubViewModel

import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.fragments.HomeFragment
import com.example.europeesaanrijdingsformulier.profile.License
import com.example.europeesaanrijdingsformulier.profile.Profile
import kotlinx.android.synthetic.main.fragment_report_algemeen_b.*


class ReportAlgemeenBFragment : Fragment() {

    private lateinit var report: Report
    private var profile: Profile? = null
    //private lateinit var viewModel: HubViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //viewModel = ViewModelProviders.of(activity!!).get(HubViewModel::class.java)
        // Inflate the layout for this fragment
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
            val firstName = this.textedit_algemeen_b_firstname.text.toString()
            val lastName = this.textedit_algemeen_b_lastname.text.toString()
            val email = this.textedit_algemeen_b_email.text.toString()


            var profiles: List<Profile>
            //val profielb = viewModel.postProfile(Profile(1,firstName,lastName,email)).blockingFirst()
            //val profiela = viewModel.postProfile(this.report.profiles.first()).blockingFirst()
            if (profile != null) {
                profiles = listOf(
                    this.report.profiles.first(),
                    Profile(1, firstName, lastName, email, profile?.license, profile?.vehicles)
                )
            } else {
                profiles = listOf(this.report.profiles.first(), Profile(1, firstName, lastName, email))
            }

            report.profiles = profiles

            //viewModel.postReport(report)
            val reportLicenseBFragment = ReportLicenseBFragment()
            reportLicenseBFragment.addObject(report)
            this.fragmentManager!!.beginTransaction()
                .replace(R.id.container_main, reportLicenseBFragment)
                //.addToBackStack(null)
                .commit()
        }
    }

    fun addReport(item: Report) {
        this.report = item
    }

    fun addProfile(item: Profile) {
        this.profile = item
    }


}
