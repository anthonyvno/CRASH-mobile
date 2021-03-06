package com.example.europeesaanrijdingsformulier.report


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.insurer.Insurer
import com.example.europeesaanrijdingsformulier.profile.Insurance
import com.example.europeesaanrijdingsformulier.utils.DatePickerManager
import com.example.europeesaanrijdingsformulier.utils.PrefManager
import com.example.europeesaanrijdingsformulier.utils.SpinnerManager
import com.example.europeesaanrijdingsformulier.utils.Validator
import kotlinx.android.synthetic.main.fragment_report_vehicle_insurance_b.*
import java.util.*


class ReportVehicleInsuranceBFragment : Fragment() {

    private lateinit var report: Report
    private lateinit var insurerName: String
    private lateinit var insurers: List<Insurer>
    private lateinit var prefManager: PrefManager
    private val spinnerManager = SpinnerManager()
    private val datePickerManager = DatePickerManager()
    private val validator= Validator()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //prefManager = PrefManager(activity)
        prefManager = PrefManager(activity!!)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_vehicle_insurance_b, container, false)
    }

    override fun onStart() {
        super.onStart()
        insurers = prefManager.getInsurers()!!
        val option = spinnerManager.instantiateSpinner(
            activity!!, R.id.spinner_report_vehicle_insurance_b_insurer,
            insurers.map { insurer -> insurer.name }.toTypedArray()
        )

        val adapter = option.adapter as ArrayAdapter<String>

        if (report.profiles.last().vehicles?.first()?.insurance?.insurer != null) {
            val spinnerPosition =
                adapter.getPosition(report.profiles.last().vehicles?.first()?.insurance!!.insurer!!.name)
            option.setSelection(spinnerPosition)
        }

        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                insurerName = adapter.getItem(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        datePickerManager.instantiateDatePicker(activity!!, R.id.textedit_report_vehicle_insurance_b_expires)
        if (report.profiles.last().vehicles?.first()?.insurance != null) {
            fillInTextFields()
        }
        button_report_vehicle_insurance_b_confirm.setOnClickListener {
            if(isValidated()){
                val date = textedit_report_vehicle_insurance_b_expires.text.toString()
                val dateSplit = date.split("/")
                val dateExpires = Date(
                    dateSplit[2].toInt() - 1900, dateSplit[1].toInt() - 1, dateSplit[0].toInt() + 1
                )
                val insurer4 = insurers.find { insurer -> insurer.name == insurerName }
                val insurance = Insurance(
                    1,
                    textedit_report_vehicle_insurance_b_insuranceNumber.text.toString(),
                    textedit_report_vehicle_insurance_b_greenCard.text.toString(),
                    textedit_report_vehicle_insurance_b_email.text.toString(),
                    dateExpires,
                    textedit_report_vehicle_insurance_b_phone.text.toString(),
                    insurer4
                )

                report.profiles.last().vehicles?.first()?.insurance = insurance

                val fragment = ReportDamageBFragment()
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
    }

    private fun fillInTextFields() {
        textedit_report_vehicle_insurance_b_email.setText(report.profiles.last().vehicles?.first()?.insurance!!.emailAgency)
        textedit_report_vehicle_insurance_b_phone.setText(report.profiles.last().vehicles?.first()?.insurance!!.phoneAgency)
        textedit_report_vehicle_insurance_b_greenCard.setText(report.profiles.last().vehicles?.first()?.insurance!!.greenCardNumber)
        textedit_report_vehicle_insurance_b_insuranceNumber.setText(report.profiles.last().vehicles?.first()?.insurance!!.insuranceNumber)
        if (report.profiles.last().vehicles?.first()?.insurance!!.expires != null) {
            val expiresYear = report.profiles.last().vehicles?.first()?.insurance!!.expires!!.year + 1900
            val expiresMonth = report.profiles.last().vehicles?.first()?.insurance!!.expires!!.month + 1
            val expiresDate = report.profiles.last().vehicles?.first()?.insurance!!.expires!!.date - 1
            val expiresvalue = ("" + expiresDate.toString() + "/" +
                    expiresMonth.toString() + "/" + expiresYear.toString())
            textedit_report_vehicle_insurance_b_expires.setText(expiresvalue)
        }

    }

    fun addObject(item: Report) {
        this.report = item
    }
    private fun isValidated():Boolean{
        if(validator.isNotEmpty(textedit_report_vehicle_insurance_b_expires.text.toString())
            && validator.isValidEmail(textedit_report_vehicle_insurance_b_email.text.toString())){
            return true
        } else {
            if(!validator.isNotEmpty(textedit_report_vehicle_insurance_b_expires.text.toString())){
                textedit_report_vehicle_insurance_b_expires.error = "Datum moet ingevuld zijn."
            }
            if(!validator.isValidEmail(textedit_report_vehicle_insurance_b_email.text.toString())){
                textedit_report_vehicle_insurance_b_email.error = "Geen geldig e-mailadres."
            }
        }
        return false
    }


}
