package com.example.europeesaanrijdingsformulier.utils

import android.support.v4.app.FragmentActivity
import android.widget.ArrayAdapter
import android.widget.Spinner

class SpinnerManager {

    fun instantiateSpinner(activity: FragmentActivity?, id:Int, arrayId: Array<String> ):Spinner{
        val option = activity!!.findViewById<Spinner>(id)
        val adapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, arrayId)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        option.adapter = adapter

        return option
    }
}