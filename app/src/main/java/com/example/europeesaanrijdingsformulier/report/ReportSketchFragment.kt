package com.example.europeesaanrijdingsformulier.report


import android.annotation.SuppressLint
import android.content.res.ColorStateList

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.europeesaanrijdingsformulier.R
import kotlinx.android.synthetic.main.fragment_report_sketch.*


class ReportSketchFragment : Fragment() {

    private lateinit var report: Report


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_sketch, container, false)
    }

    @SuppressLint("RestrictedApi")
    override fun onStart() {
        super.onStart()


/*
        val bitmap = BitmapFactory.decodeResource(context!!.getResources(),
            R.drawable.redcar)
        val destBitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true)
        val canvas = Canvas(destBitmap)

        drawview_report_sketch.
*/
        button_sketch_draw.setOnClickListener{
            drawview_report_sketch.toggleDrawing()
            if(button_sketch_redo.visibility == View.INVISIBLE ){
                button_sketch_draw.backgroundTintList=ColorStateList.valueOf(resources.getColor(R.color.lightBlue))
                button_sketch_redo.visibility = View.VISIBLE
                button_sketch_undo.visibility = View.VISIBLE
            } else {
                button_sketch_draw.backgroundTintList=ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
                button_sketch_redo.visibility = View.INVISIBLE
                button_sketch_undo.visibility = View.INVISIBLE
            }

        }
        button_sketch_undo.setOnClickListener{
            drawview_report_sketch.undo()
        }
        button_sketch_redo.setOnClickListener{
            drawview_report_sketch.redo()
        }
        button_sketch_clear.setOnClickListener{
            drawview_report_sketch.clearCanvas()
        }
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
