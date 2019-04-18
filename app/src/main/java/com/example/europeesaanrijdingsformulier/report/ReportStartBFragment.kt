package com.example.europeesaanrijdingsformulier.report


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.utils.PrefManager
import com.example.europeesaanrijdingsformulier.utils.QRManager
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.fragment_report_start_b.*

class ReportStartBFragment : Fragment() {

    private lateinit var report: Report
    private lateinit var prefManager: PrefManager
    private val qrManager = QRManager()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prefManager = PrefManager(activity)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_start_b, container, false)
    }


    override fun onStart() {
        super.onStart()

        button_report_start_b_manual.setOnClickListener{
            val fragment = ReportAlgemeenBFragment()
            prefManager.saveReport(report)
            fragment.addReport(report)
            this.fragmentManager!!.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right)
                .replace(R.id.container_main, fragment)
                .addToBackStack(null)
                .commit()
        }
        button_report_start_b_QR.setOnClickListener{
            prefManager.saveReport(report)
            val scanner = IntentIntegrator.forSupportFragment(this).initiateScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            Log.d("qr result partij B:", result.contents)
            if (result != null) {
                if (result.contents == null) {
                    Toast.makeText(activity, "Cancelled", Toast.LENGTH_LONG).show()
                } else {
                    val fragment = ReportAlgemeenBFragment()
                    //val gson = Gson()
                    if (result.contents.startsWith("{\"version\"")) {
                        fragment.addProfile(qrManager.handleGreenCardScan(result.contents))
                        fragment.addReport(report)
                        fragmentManager!!.beginTransaction()
                            .replace(R.id.container_main, fragment)
                            .addToBackStack(null)
                            .commitAllowingStateLoss()
                    } else if(result.contents.startsWith("{\"email\"")){
                        fragment.addProfile(qrManager.handleProfileScan(result.contents))
                        fragment.addReport(report)
                        fragmentManager!!.beginTransaction()
                            .replace(R.id.container_main, fragment)
                            .addToBackStack(null)
                            .commitAllowingStateLoss()
                    } else {
                        Toast.makeText(activity, Html.fromHtml("<font color='#FF0000' ><b>" + "Geen geldige QR-code" + "</b></font>") , Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }

        }
    }

    fun addObject(item: Report) {
        this.report = item
    }
}
