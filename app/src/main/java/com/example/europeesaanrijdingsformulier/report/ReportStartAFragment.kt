package com.example.europeesaanrijdingsformulier.report

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.profile.Profile
import com.example.europeesaanrijdingsformulier.utils.PrefManager
import kotlinx.android.synthetic.main.fragment_report_start_a.*

class ReportStartAFragment : Fragment() {

    private lateinit var report: Report
    private lateinit var prefManager: PrefManager
    private lateinit var profile: Profile


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prefManager = PrefManager(activity)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_start_a, container, false)
    }

    override fun onStart() {
        super.onStart()

        button_report_start_a_manual.setOnClickListener{
            val fragment = ReportAlgemeenAFragment()
            fragment.addReport(report)
            this.fragmentManager!!.beginTransaction()
                .replace(R.id.container_main, fragment)
                .addToBackStack(null)
                .commit()
        }

        button_report_start_a_profile.setOnClickListener{
            val fragment = ReportAlgemeenAFragment()
            //voeg profile toe
            profile = prefManager.getProfile()!!
            fragment.addReport(report)
            fragment.addProfile(profile)
            this.fragmentManager!!.beginTransaction()
                .replace(R.id.container_main, fragment)
                .addToBackStack(null)
                .commit()
        }
        }

    fun addObject(item: Report) {
        this.report = item
    }




}
