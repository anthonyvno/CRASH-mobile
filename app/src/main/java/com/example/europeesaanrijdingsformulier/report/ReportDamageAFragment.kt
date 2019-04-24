package com.example.europeesaanrijdingsformulier.report


import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.utils.PrefManager
import kotlinx.android.synthetic.main.fragment_report_damage_a.*


class ReportDamageAFragment : Fragment() {

    private lateinit var report: Report
    private lateinit var prefManager: PrefManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prefManager = PrefManager(activity)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_damage_a, container, false)
    }

    override fun onStart() {
        var drawable: Int
        when(report.profiles.get(0).vehicles?.get(0)?.type){
            "Auto" -> drawable = R.drawable.redcar
            "Motorfiets" -> drawable = R.drawable.redmotorcycle
            "Bus" -> drawable = R.drawable.redbus
            "Vrachtwagen" -> drawable = R.drawable.redtruck
            else -> drawable = R.drawable.redcar
        }
        damageview_a.setVehicle(drawable)
        super.onStart()

        button_report_damage_a_undo.setOnClickListener{
            damageview_a.undo()
        }
        button_report_damage_a_redo.setOnClickListener{
            damageview_a.redo()
        }
        button_report_damage_a_clear.setOnClickListener{
            damageview_a.clearCanvas()
        }

        button_report_damage_a_confirm.setOnClickListener{

            AlertDialog.Builder(activity)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Profiel opslaan")
                .setMessage("Wilt u dit profiel opslaan?\n Een nieuw profiel zal aangemaakt worden of uw oud profiel zal vervangen worden.")
                .setPositiveButton(
                    "Ja",
                    DialogInterface.OnClickListener { dialog, which ->
                        prefManager.saveLicense(report.profiles[0].license!!)
                        var vehicles = mutableListOf(report.profiles[0].vehicles!![0])
                        prefManager.saveVehicles(vehicles)
                        prefManager.saveProfile(report.profiles[0])
                    })
                .setNegativeButton("Nee", null)
                .show()

            val fragment = ReportStartBFragment()
            fragment.addObject(report)
            this.fragmentManager!!.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right)
                .replace(R.id.container_main, fragment,"startB")
                //.addToBackStack(null)
                .commit()
        }
    }

    fun addObject(item: Report) {
        this.report = item
    }


}
