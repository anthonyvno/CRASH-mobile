package com.example.europeesaanrijdingsformulier.utils

import android.app.DatePickerDialog
import android.support.v4.app.FragmentActivity
import android.widget.EditText
import com.example.europeesaanrijdingsformulier.R
import java.util.*

class DatePickerManager {
    fun instantiateDatePicker(activity: FragmentActivity?, id: Int){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val datepicker = activity!!.findViewById<EditText>(id)
        datepicker.setOnClickListener(){
            val dpd = DatePickerDialog(activity, R.style.MySpinnerDatePickerStyle, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                var monthOfYear2 = monthOfYear +1
                datepicker.setText("" + dayOfMonth + "/" + monthOfYear2 + "/" + year)

            }, year, month, day)
            dpd.show()
        }
    }
}