package com.example.europeesaanrijdingsformulier.fragments


import android.app.ProgressDialog
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.europeesaanrijdingsformulier.MainActivity

import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.report.Report
import com.example.europeesaanrijdingsformulier.ui.HubViewModel
import kotlinx.android.synthetic.main.fragment_local_report.*


class LocalReportFragment : Fragment() {

    private lateinit var report: Report
    private lateinit var viewModel: HubViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(activity!!).get(HubViewModel::class.java)


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_local_report, container, false)
    }

    override fun onStart() {
        super.onStart()

        val bytePdf = viewModel.createPdf(report).blockingFirst()
        val bytes = Base64.decode(bytePdf.pdfReport, Base64.DEFAULT)
        pdfView.fromBytes(bytes).load()

        report.pdfReport = bytePdf.pdfReport

    }



    fun addObject(item: Report) {
        this.report = item
    }


}
