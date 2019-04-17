package com.example.europeesaanrijdingsformulier.report


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.utils.PrefManager
import kotlinx.android.synthetic.main.fragment_report_circumstances2.*


class ReportCircumstances2Fragment : Fragment() {

    private lateinit var report: Report
    private lateinit var prefManager: PrefManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        prefManager = PrefManager(activity!!)

        return inflater.inflate(R.layout.fragment_report_circumstances2, container, false)
    }

    override fun onStart() {
        super.onStart()

        button_report_circ_save.setOnClickListener {

            report.circumstances!![0]+= (booleanArrayOf(
                switch_circ_9a.isChecked,
                switch_circ_10a.isChecked,
                switch_circ_11a.isChecked,
                switch_circ_12a.isChecked,
                switch_circ_13a.isChecked,
                switch_circ_14a.isChecked,
                switch_circ_15a.isChecked,
                switch_circ_16a.isChecked,
                switch_circ_17a.isChecked))

            report.circumstances!![1] += (booleanArrayOf(
                switch_circ_9b.isChecked,
                switch_circ_10b.isChecked,
                switch_circ_11b.isChecked,
                switch_circ_12b.isChecked,
                switch_circ_13b.isChecked,
                switch_circ_14b.isChecked,
                switch_circ_15b.isChecked,
                switch_circ_16b.isChecked,
                switch_circ_17b.isChecked))

            prefManager.saveReport(report)

            val fragment = ReportSketchFragment()
            fragment.addObject(report)
            this.fragmentManager!!.beginTransaction()
                .setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
                .replace(R.id.container_main, fragment)
                //.addToBackStack(null)
                .commit()
        }
    }


    fun addObject(item: Report) {
        this.report = item
    }


}
