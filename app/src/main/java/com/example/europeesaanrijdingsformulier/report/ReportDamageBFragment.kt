package com.example.europeesaanrijdingsformulier.report


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.europeesaanrijdingsformulier.R
import kotlinx.android.synthetic.main.fragment_report_damage_b.*

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
        var drawable: Int
        when (report.profiles.get(1).vehicles?.get(0)?.type) {
            "Auto" -> drawable = R.drawable.greencar
            "Motorfiets" -> drawable = R.drawable.greenmotorcycle
            "Bus" -> drawable = R.drawable.greenbus
            "Vrachtwagen" -> drawable = R.drawable.greentruck
            else -> drawable = R.drawable.greencar
        }
        damageview_b.setVehicle(drawable)
        super.onStart()


        button_report_damage_b_undo.setOnClickListener{
            damageview_b.undo()
        }
        button_report_damage_b_redo.setOnClickListener{
            damageview_b.redo()
        }
        button_report_damage_b_clear.setOnClickListener{
            damageview_b.clearCanvas()
        }
        button_report_damage_b_confirm.setOnClickListener {


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
