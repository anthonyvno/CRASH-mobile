package com.example.europeesaanrijdingsformulier.report


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.europeesaanrijdingsformulier.R
import kotlinx.android.synthetic.main.fragment_report_sketch.*
import kotlinx.android.synthetic.main.fragment_report_sketch.view.*


class ReportSketchFragment : Fragment() {

    private lateinit var report: Report


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_sketch, container, false)
    }

    override fun onStart() {
        super.onStart()


/*
        val bitmap = BitmapFactory.decodeResource(context!!.getResources(),
            R.drawable.redcar)
        val destBitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true)
        val canvas = Canvas(destBitmap)

        drawview_report_sketch.
*/
        button_report_sketch_confirm.setOnClickListener{

            val fragment = ReportOverviewFragment()
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
