package com.example.europeesaanrijdingsformulier.report


import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.europeesaanrijdingsformulier.R
import kotlinx.android.synthetic.main.fragment_report_sketch.*
import java.io.ByteArrayOutputStream


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

        val drawableA: Int = when(report.profiles[0].vehicles?.get(0)?.type){
            "Auto" -> R.drawable.redcar
            "Motorfiets" -> R.drawable.redmotorcycle
            "Bus" -> R.drawable.redbus
            "Vrachtwagen" -> R.drawable.redtruck
            else -> R.drawable.redcar
        }
        val drawableB: Int = when(report.profiles[1].vehicles?.get(0)?.type){
            "Auto" -> R.drawable.greencar
            "Motorfiets" -> R.drawable.greenmotorcycle
            "Bus" -> R.drawable.greenbus
            "Vrachtwagen" -> R.drawable.greentruck
            else -> R.drawable.greencar
        }

        drawview_report_sketch.setSituation(drawableA,drawableB)

        super.onStart()

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

            val bitmap = drawview_report_sketch.getBitmap()
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG,100,stream)
            val bytearray = stream.toByteArray()
            val encoded = Base64.encodeToString(bytearray,Base64.DEFAULT)

            report.sketch = encoded


            val fragment = ReportImageFragment()
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

    override fun onResume() {
        super.onResume()

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onPause() {
        super.onPause()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
    }

    fun addObject(item: Report) {
        this.report = item
    }


}
