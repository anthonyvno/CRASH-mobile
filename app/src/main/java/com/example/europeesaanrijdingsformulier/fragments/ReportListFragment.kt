package com.example.europeesaanrijdingsformulier.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.profile.ProfileVehicleDetailFragment
import com.example.europeesaanrijdingsformulier.profile.Vehicle
import com.example.europeesaanrijdingsformulier.profile.VehicleViewAdapter
import com.example.europeesaanrijdingsformulier.report.Report
import com.example.europeesaanrijdingsformulier.utils.PrefManager
import kotlinx.android.synthetic.main.fragment_profile_vehicle_list.*
import kotlinx.android.synthetic.main.fragment_report_list.*

class ReportListFragment : Fragment() {

    private lateinit var adapter: ReportViewAdapter
    private lateinit var prefManager: PrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prefManager = PrefManager(activity)
        return inflater.inflate(R.layout.fragment_report_list, container, false)
    }

    override fun onStart() {
        super.onStart()
        val reports = prefManager.getReport()

        if(reports!=null){
            adapter =ReportViewAdapter(this, listOf(reports))
            fragment_report_list.adapter = adapter
        } else {
            adapter = ReportViewAdapter(this, emptyList())
            fragment_report_list.adapter = adapter

        }
    }

    fun startNewFragmentForDetail(item: Report) {
     /*   val profileVehicleDetailFragment = ProfileVehicleDetailFragment()
        profileVehicleDetailFragment.addObject(item)
        this.fragmentManager!!.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right)
            .replace(R.id.container_main,profileVehicleDetailFragment,"detail")
            .addToBackStack("list_to_detail")
            .commit()*/
    }


}
