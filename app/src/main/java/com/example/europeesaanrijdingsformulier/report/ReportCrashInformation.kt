package com.example.europeesaanrijdingsformulier.report


import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

import com.example.europeesaanrijdingsformulier.R
import kotlinx.android.synthetic.main.fragment_report_crash_information.*
import java.util.*


class ReportCrashInformation : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_crash_information, container, false)
    }

    override fun onStart() {
        super.onStart()
        instantiateDatePicker()
        button_report_crash_information_confirm.setOnClickListener{
            this.fragmentManager!!.beginTransaction()
                .replace(R.id.container_main, ReportAlgemeenAFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun instantiateDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datepicker = activity!!.findViewById<EditText>(R.id.textedit_report_crash_information_date)

        datepicker.setOnClickListener(){
            val dpd = DatePickerDialog(activity, R.style.MySpinnerDatePickerStyle, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                datepicker.setText("" + dayOfMonth + "/" + monthOfYear + "/" + year)

            }, year, month, day)



            dpd.show()
        }

    }

}
