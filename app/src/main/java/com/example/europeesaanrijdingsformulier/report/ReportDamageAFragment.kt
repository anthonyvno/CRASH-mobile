package com.example.europeesaanrijdingsformulier.report


import android.app.AlertDialog
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.profile.Vehicle
import com.example.europeesaanrijdingsformulier.utils.PrefManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_report_damage_a.*
import java.io.ByteArrayOutputStream


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
        val drawable: Int = when(report.profiles[0].vehicles?.get(0)?.type){
            "Auto" -> R.drawable.redcar
            "Motorfiets" -> R.drawable.redmotorcycle
            "Bus" -> R.drawable.redbus
            "Vrachtwagen" -> R.drawable.redtruck
            else -> R.drawable.redcar
        }
        damageview_a.setVehicle(drawable)
        super.onStart()

        button_report_damage_a_clear.setOnClickListener{
            damageview_a.clearCanvas()
        }

        button_report_damage_a_confirm.setOnClickListener{

            val bitmapA = damageview_a.getBitmap()
            val streamA = ByteArrayOutputStream()
            bitmapA.compress(Bitmap.CompressFormat.PNG,100,streamA)
            val bytearrayA = streamA.toByteArray()
            val encodedA = Base64.encodeToString(bytearrayA, Base64.DEFAULT)

            report.damageIndications = arrayOf(encodedA)
            report.remarks = arrayOf(text_report_damage_a_comment.text.toString())

            AlertDialog.Builder(activity)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Profiel opslaan")
                .setMessage("Wilt u dit profiel opslaan?\n Een nieuw profiel zal aangemaakt worden of uw oud profiel zal vervangen worden.")
                .setPositiveButton(
                    "Ja"
                ) { dialog, which ->
                    val gson = Gson()
                    println(gson.toJson(report.profiles))

                    prefManager.saveLicense(report.profiles[0].license!!)
                    val vehicles = report.profiles[0].vehicles!! as MutableList<Vehicle>
                    prefManager.saveVehicles(vehicles)
                    prefManager.saveProfile(report.profiles[0])
                }
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
