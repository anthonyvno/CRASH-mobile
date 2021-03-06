package com.example.europeesaanrijdingsformulier.report


import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.europeesaanrijdingsformulier.R
import kotlinx.android.synthetic.main.fragment_report_damage_b.*
import java.io.ByteArrayOutputStream

class ReportDamageBFragment : Fragment() {

    private lateinit var report: Report

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_damage_b, container, false)
    }

    override fun onStart() {
        val drawable: Int = when (report.profiles[1].vehicles?.get(0)?.type) {
            "Auto" -> R.drawable.greencar
            "Motorfiets" -> R.drawable.greenmotorcycle
            "Bus" -> R.drawable.greenbus
            "Vrachtwagen" -> R.drawable.greentruck
            else -> R.drawable.greencar
        }
        damageview_b.setVehicle(drawable)
        super.onStart()

        button_report_damage_b_clear.setOnClickListener{
            damageview_b.clearCanvas()
        }
        button_report_damage_b_confirm.setOnClickListener {

            val bitmapA = damageview_b.getBitmap()
            val streamA = ByteArrayOutputStream()
            bitmapA.compress(Bitmap.CompressFormat.PNG,100,streamA)
            val bytearrayA = streamA.toByteArray()
            val encodedA = Base64.encodeToString(bytearrayA, Base64.DEFAULT)

            report.damageIndications = arrayOf(report.damageIndications!![0],encodedA)
            report.remarks = arrayOf(report.remarks!![0],text_report_damage_b_comment.text.toString())

            val fragment = ReportCircumstancesFragment()
            fragment.addObject(report)
            this.fragmentManager!!.beginTransaction()
                .setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
                .replace(R.id.container_main, fragment,"circumstances")
                //.addToBackStack(null)
                .commit()
        }
    }

    fun addObject(item: Report) {
        this.report = item
    }


}
