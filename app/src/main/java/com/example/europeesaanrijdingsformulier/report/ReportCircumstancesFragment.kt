package com.example.europeesaanrijdingsformulier.report


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.europeesaanrijdingsformulier.R
import kotlinx.android.synthetic.main.fragment_report_circumstances.*


class ReportCircumstancesFragment : Fragment() {

    private lateinit var report: Report

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_circumstances, container, false)
    }

    override fun onStart() {
        super.onStart()

        button_report_circ_confirm.setOnClickListener {


            report.circumstances!![0] = booleanArrayOf(
                switch_circ_1a.isChecked,
                switch_circ_2a.isChecked,
                switch_circ_3a.isChecked,
                switch_circ_4a.isChecked,
                switch_circ_5a.isChecked,
                switch_circ_6a.isChecked,
                switch_circ_7a.isChecked,
                switch_circ_8a.isChecked
            )

            report.circumstances!![1] = booleanArrayOf(
                switch_circ_1b.isChecked,
                switch_circ_2b.isChecked,
                switch_circ_3b.isChecked,
                switch_circ_4b.isChecked,
                switch_circ_5b.isChecked,
                switch_circ_6b.isChecked,
                switch_circ_7b.isChecked,
                switch_circ_8b.isChecked
            )


            val fragment = ReportCircumstances2Fragment()
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
