package com.example.europeesaanrijdingsformulier.report


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.profile.License
import com.example.europeesaanrijdingsformulier.utils.DatePickerManager
import com.example.europeesaanrijdingsformulier.utils.SpinnerManager
import kotlinx.android.synthetic.main.fragment_report_license_b.*


class ReportLicenseBFragment : Fragment() {

    private lateinit var report: Report
    private var category: String = ""
    private val spinnerManager = SpinnerManager()
    private val datePickerManager = DatePickerManager()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_license_b, container, false)
    }

    override fun onStart() {
        super.onStart()
        val option = spinnerManager.instantiateSpinner(activity!!,R.id.spinner_report_license_b_category, resources.getStringArray(R.array.licenseCategory))
        val adapter = option.adapter as ArrayAdapter<String>
        if (report.profiles.last().license?.category != null) {
            val spinnerPosition = adapter.getPosition(report.profiles.last().license?.category)
            option.setSelection(spinnerPosition)
        }

        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                category = adapter.getItem(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        datePickerManager.instantiateDatePicker(activity!!,R.id.textedit_report_license_b_expires)
        if (report.profiles.last().license != null) {
            fillInTextFields()
        }
        button_report_license_b_confirm.setOnClickListener {

            val expires = textedit_report_license_b_licenseNumber.text.toString()
            val licenseNumber = textedit_report_license_b_licenseNumber.text.toString()

            report.profiles.last().license = License(1, category, licenseNumber, expires)

            val reportVehicleDetailBFragment = ReportVehicleDetailBFragment()
            this.fragmentManager!!.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right)
                .replace(R.id.container_main, reportVehicleDetailBFragment)
                .addToBackStack(null)
                .commit()
            reportVehicleDetailBFragment.addObject(report)

        }
    }

    private fun fillInTextFields() {
        textedit_report_license_b_expires.setText(report.profiles.last().license?.expires)
        textedit_report_license_b_licenseNumber.setText(report.profiles.last().license?.licenseNumber)
    }

    fun addObject(item: Report) {
        this.report = item
    }


}
