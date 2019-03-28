package com.example.europeesaanrijdingsformulier.report


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.europeesaanrijdingsformulier.R
import com.google.gson.Gson
import com.itextpdf.text.Image
import kotlinx.android.synthetic.main.fragment_report_overview.*
import java.io.FileInputStream


class ReportOverviewFragment : Fragment() {

    private lateinit var report: Report
    private lateinit var image:Image

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_overview, container, false)
    }

    override fun onStart() {
        super.onStart()
        val gson = Gson()

/*
        val decodedString = Base64.decode(report.imagePDF, Base64.DEFAULT)
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        Log.d("bytearray",gson.toJson(decodedByte))

        image_report_overview_pdf.setImageBitmap(decodedByte)
        */
        pdfview_report_confirmation.fromFile(report.imagePDF).load()






        /*

        Log.d("bytearray:",report.imagePDF!!.size.toString())
        val bmp = BitmapFactory.decodeByteArray(report.imagePDF, 0, report.imagePDF!!.size)
        image_report_overview_pdf.setImageBitmap(
            Bitmap.createBitmap(bmp)
        )
        */


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
                //.addToBackStack(null)
                .commit()
        }
    }

    fun addObject(item: Report) {
        this.report = item
    }


}
