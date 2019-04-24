package com.example.europeesaanrijdingsformulier.report


import android.app.AlertDialog
import android.app.DatePickerDialog
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import com.example.anthonyvannoppen.androidproject.ui.HubViewModel

import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.insurer.Insurer
import com.example.europeesaanrijdingsformulier.profile.Insurance
import com.example.europeesaanrijdingsformulier.profile.Vehicle
import com.example.europeesaanrijdingsformulier.utils.DatePickerManager
import com.example.europeesaanrijdingsformulier.utils.PrefManager
import com.example.europeesaanrijdingsformulier.utils.SpinnerManager
import com.example.europeesaanrijdingsformulier.utils.Validator
import kotlinx.android.synthetic.main.fragment_report_vehicle_insurance_a.*
import java.util.*

class ReportVehicleInsuranceAFragment : Fragment() {

    private lateinit var report: Report
    private lateinit var insurerName: String
    private lateinit var insurers: List<Insurer?>
    private val spinnerManager = SpinnerManager()
    private val datePickerManager = DatePickerManager()
    private val validator = Validator()
    private lateinit var prefManager: PrefManager



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prefManager = PrefManager(activity)
        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_vehicle_insurance_a, container, false)
    }

    override fun onStart() {
        super.onStart()
        insurers = prefManager.getInsurers()!!
        val option = spinnerManager.instantiateSpinner(activity!!,R.id.spinner_report_vehicle_insurance_a_insurer,
            insurers.map { insurer -> insurer!!.name }.toTypedArray())

        val adapter = option.adapter as ArrayAdapter<String>

        if (report.profiles.first().vehicles?.first()?.insurance?.insurer != null) {
            val spinnerPosition = adapter.getPosition(report.profiles.first().vehicles?.first()?.insurance!!.insurer!!.name)
            option.setSelection(spinnerPosition)
        }

        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                insurerName = adapter.getItem(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        datePickerManager.instantiateDatePicker(activity!!,R.id.textedit_report_vehicle_insurance_a_expires)
        if (report.profiles.first().vehicles?.first()?.insurance != null) {
            fillInTextFields()
        }
        button_report_vehicle_insurance_a_confirm.setOnClickListener{
            if(isValidated()){
                val date = textedit_report_vehicle_insurance_a_expires.text.toString()
                val dateSplit = date.split("/")
                val dateExpires = Date(
                    dateSplit[2].toInt() - 1900, dateSplit[1].toInt() -1, dateSplit[0].toInt() +1
                )
                val insurer4 = insurers.find { insurer -> insurer!!.name == insurerName }
                var insurance = Insurance(
                    1,
                    textedit_report_vehicle_insurance_a_insuranceNumber.text.toString(),
                    textedit_report_vehicle_insurance_a_greenCard.text.toString(),
                    textedit_report_vehicle_insurance_a_email.text.toString(),
                    dateExpires,
                    textedit_report_vehicle_insurance_a_phone.text.toString(),
                    insurer4
                )

                report.profiles.first().vehicles?.first()?.insurance = insurance

                val fragment = ReportDamageAFragment()
                fragment.addObject(report)
                this.fragmentManager!!.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right)
                    .replace(R.id.container_main, fragment)
                    .addToBackStack(null)
                    .commit()
            }

        }
    }

    private fun fillInTextFields() {
        textedit_report_vehicle_insurance_a_email.setText(report.profiles.first().vehicles?.first()?.insurance!!.emailAgency)
        textedit_report_vehicle_insurance_a_phone.setText(report.profiles.first().vehicles?.first()?.insurance!!.phoneAgency)
        textedit_report_vehicle_insurance_a_greenCard.setText(report.profiles.first().vehicles?.first()?.insurance!!.greenCardNumber)
        textedit_report_vehicle_insurance_a_insuranceNumber.setText(report.profiles.first().vehicles?.first()?.insurance!!.insuranceNumber)
        if(report.profiles.first().vehicles?.first()?.insurance!!.expires != null){
            val expiresYear = report.profiles.first().vehicles?.first()?.insurance!!.expires!!.year + 1900
            val expiresMonth = report.profiles.first().vehicles?.first()?.insurance!!.expires!!.month + 1
            val expiresDate = report.profiles.first().vehicles?.first()?.insurance!!.expires!!.date - 1
            val expiresvalue = (""+expiresDate.toString() + "/" +
                    expiresMonth.toString() + "/" + expiresYear.toString() )
            textedit_report_vehicle_insurance_a_expires.setText(expiresvalue)
        }
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

    fun isValidated():Boolean{
        if(validator.isNotEmpty(textedit_report_vehicle_insurance_a_expires.text.toString())
            && validator.isValidEmail(textedit_report_vehicle_insurance_a_email.text.toString())){
            return true
        } else {
            if(!validator.isNotEmpty(textedit_report_vehicle_insurance_a_expires.text.toString())){
                textedit_report_vehicle_insurance_a_expires.setError("Datum moet ingevuld zijn.")
            }
            if(!validator.isValidEmail(textedit_report_vehicle_insurance_a_email.text.toString())){
                textedit_report_vehicle_insurance_a_email.setError("Geen geldig e-mailadres.")
            }
        }
        return false
    }
}
