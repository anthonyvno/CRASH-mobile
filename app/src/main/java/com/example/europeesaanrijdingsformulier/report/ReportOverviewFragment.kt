package com.example.europeesaanrijdingsformulier.report


import android.arch.lifecycle.ViewModelProviders
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.anthonyvannoppen.androidproject.ui.HubViewModel
import com.example.europeesaanrijdingsformulier.R
import com.itextpdf.text.Image
import kotlinx.android.synthetic.main.fragment_report_overview.*


class ReportOverviewFragment : Fragment() {

    private lateinit var report: Report
    private lateinit var image:Image
    private lateinit var viewModel: HubViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(activity!!).get(HubViewModel::class.java)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_overview, container, false)
    }

    override fun onStart() {
        super.onStart()

        val bytePdf = viewModel.createPdf(report).blockingFirst()
        val bytes = Base64.decode(bytePdf.pdfReport, Base64.DEFAULT)
        pdfView.fromBytes(bytes).load()

        report.pdfReport = bytePdf.pdfReport

        button_report_overview_confirm.setOnClickListener {

            val fragment = ReportConfirmationFragment()
            fragment.addObject(report)
            this.fragmentManager!!.beginTransaction()
                .setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
                .replace(R.id.container_main, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    fun addObject(item: Report) {
        this.report = item
    }


}
