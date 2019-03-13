package com.example.europeesaanrijdingsformulier.report


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.europeesaanrijdingsformulier.R
import kotlinx.android.synthetic.main.fragment_report_start_b.*

class ReportStartBFragment : Fragment() {

    private lateinit var report: Report


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_start_b, container, false)
    }


    override fun onStart() {
        super.onStart()

        button_report_start_b_manual.setOnClickListener{
            val fragment = ReportAlgemeenBFragment()
            fragment.addObject(report)
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
