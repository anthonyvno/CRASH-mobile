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
    private lateinit var viewModel: HubViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProviders.of(activity!!).get(HubViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_algemeen_b, container, false)
    }

    override fun onStart() {
        super.onStart()

        button_algemeen_b_confirm.setOnClickListener{
            val firstName =this.textedit_algemeen_b_firstname.text.toString()
            val lastName = this.textedit_algemeen_b_lastname.text.toString()
            val email = this.textedit_algemeen_b_email.text.toString()



            val profielb = viewModel.postProfile(Profile(1,firstName,lastName,email)).blockingFirst()
            val profiela = viewModel.postProfile(this.report.profiles.first()).blockingFirst()
            val profiles = listOf(profiela,profielb)

            report = Report(1,profiles)

            viewModel.postReport(report)

            this.fragmentManager!!.beginTransaction()
                .replace(R.id.container_main, HomeFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    fun addObject(item: Report) {
        this.report = item

    }


}
