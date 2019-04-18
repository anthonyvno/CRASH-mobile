package com.example.europeesaanrijdingsformulier.report


import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner

import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.profile.License
import com.example.europeesaanrijdingsformulier.utils.DatePickerManager
import com.example.europeesaanrijdingsformulier.utils.PrefManager
import com.example.europeesaanrijdingsformulier.utils.SpinnerManager
import kotlinx.android.synthetic.main.fragment_report_license_a.*
import java.util.*


class ReportLicenseAFragment : Fragment() {

    private lateinit var report: Report
    private var category: String = ""
    private val spinnerManager = SpinnerManager()
    private val datePickerManager = DatePickerManager()
    private lateinit var prefManager: PrefManager



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prefManager = PrefManager(activity)
        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_license_a, container, false)
    }

    override fun onStart() {
        super.onStart()
        val option = spinnerManager.instantiateSpinner(
            activity!!,
            R.id.spinner_report_license_a_category,
            getResources().getStringArray(R.array.licenseCategory))
        val adapter = option.adapter as ArrayAdapter<String>

        if (report?.profiles.first().license?.category != null) {
            val spinnerPosition = adapter.getPosition(report?.profiles.first().license?.category)
            option.setSelection(spinnerPosition)
        }

        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                category = adapter.getItem(position) as String
            }


        }

        datePickerManager.instantiateDatePicker(activity!!,R.id.textedit_report_license_a_expires)
        if (report.profiles.first().license != null) {
            fillInTextFields()
        }
        button_report_license_a_confirm.setOnClickListener {

            val expires = textedit_report_license_a_expires.text.toString()
            val licenseNumber = textedit_report_license_a_licenseNumber.text.toString()

            report.profiles.first().license = License(1, category, licenseNumber, expires)

            val reportVehicleDetailAFragment = ReportVehicleDetailAFragment()
            this.fragmentManager!!.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right)
                .replace(R.id.container_main, reportVehicleDetailAFragment)
                .addToBackStack(null)
                .commit()
            reportVehicleDetailAFragment.addObject(report)

        }
    }

    private fun fillInTextFields() {
        textedit_report_license_a_expires.setText(report.profiles.first().license?.expires)
        textedit_report_license_a_licenseNumber.setText(report.profiles.first().license?.licenseNumber)
    }

    fun addObject(item: Report) {
        this.report = item
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        //menu?.clear()
        //inflater!!.inflate(R.menu.menu_main,menu)
        super.onCreateOptionsMenu(menu, inflater)

        var item = menu!!.findItem(R.id.action_belVerzekeraar)
        if(!prefManager.getVehicles().isNullOrEmpty()&&prefManager.getVehicles()?.first()?.insurance?.phoneAgency != ""){
            item.isVisible = true
        }



    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_belVerzekeraar -> {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:"+prefManager.getVehicles()?.first()?.insurance?.phoneAgency)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
