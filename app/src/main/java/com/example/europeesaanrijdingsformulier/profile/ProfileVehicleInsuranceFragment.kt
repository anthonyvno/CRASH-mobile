package com.example.europeesaanrijdingsformulier.profile


import android.app.DatePickerDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import com.example.anthonyvannoppen.androidproject.ui.HubViewModel

import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.insurer.Insurer
import com.example.europeesaanrijdingsformulier.utils.PrefManager
import com.jakewharton.rxbinding2.widget.text
import kotlinx.android.synthetic.main.fragment_profile_vehicle_detail.*
import kotlinx.android.synthetic.main.fragment_profile_vehicle_insurance.*
import kotlinx.android.synthetic.main.fragment_report_crash_information.*
import java.util.*

class ProfileVehicleInsuranceFragment : Fragment() {

    private lateinit var insurers: List<Insurer>
    private lateinit var viewModel: HubViewModel
    private lateinit var prefManager: PrefManager
    private lateinit var vehicle: Vehicle
    private lateinit var insurerName: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(activity!!).get(HubViewModel::class.java)
        prefManager = PrefManager(activity)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_vehicle_insurance, container, false)
    }


    override fun onStart() {
        super.onStart()

        instantiateSpinners()
        instantiateDatePicker()
        if (vehicle!!.insurance != null) {
            fillInTextFields()
        }
        button_profile_vehicle_insurance_confirm.setOnClickListener {
            val date = textedit_profile_vehicle_insurance_expires.text.toString()
            val dateSplit = date.split("/")
            val dateExpires = Date(
                dateSplit[2].toInt() - 1900, dateSplit[1].toInt() -1, dateSplit[0].toInt() +1
            )
            val insurer4 = insurers.find { insurer -> insurer.name == insurerName }
            var insurance = Insurance(
                1,
                textedit_profile_vehicle_insurance_insuranceNumber.text.toString(),
                textedit_profile_vehicle_insurance_greenCard.text.toString(),
                textedit_profile_vehicle_insurance_email.text.toString(),
                dateExpires,
                textedit_profile_vehicle_insurance_phone.text.toString(),
                insurer4
            )

            val vehicle2: Vehicle
            val profile = prefManager.getProfile()

            if (vehicle!!.insurance == null) {
                vehicle.insurance = viewModel.postInsurance(insurance).blockingFirst()
                vehicle2 = viewModel.postVehicle(vehicle!!).blockingFirst()
            } else {
                vehicle.insurance = viewModel.updateInsurance(insurance).blockingFirst()
                vehicle2 = viewModel.updateVehicle(vehicle!!).blockingFirst()
            }


            var vehicles = prefManager.getVehicles()
//            Log.d("testpurp3",vehicles!!.first().insurance!!.emailAgency)
            if (vehicles != null) {
                var comparevehicle = vehicles.find { it.id == vehicle2.id }
                if (comparevehicle == null) {
                    vehicles.add(vehicle2)
                } else {
                    vehicles.remove(comparevehicle)
                    vehicles.add(vehicle2)
                }
            } else {
                vehicles = mutableListOf<Vehicle>(vehicle2)
            }

            prefManager.saveVehicles(vehicles)
            profile!!.vehicles = vehicles
            prefManager.saveProfile(profile)
            viewModel.updateProfile(profile).blockingFirst()

            this.fragmentManager!!.beginTransaction()
                .setCustomAnimations(R.anim.abc_fade_in,R.anim.abc_fade_out,R.anim.abc_fade_in,R.anim.abc_fade_out)
                .replace(R.id.container_main, ProfileSummaryFragment())
                //.addToBackStack(null)
                .commit()
        }


    }

    private fun fillInTextFields() {
        textedit_profile_vehicle_insurance_email.setText(vehicle!!.insurance!!.emailAgency)
        textedit_profile_vehicle_insurance_phone.setText(vehicle!!.insurance!!.phoneAgency)
        textedit_profile_vehicle_insurance_greenCard.setText(vehicle!!.insurance!!.greenCardNumber)
        textedit_profile_vehicle_insurance_insuranceNumber.setText(vehicle!!.insurance!!.insuranceNumber)
        val expiresYear = vehicle.insurance!!.expires!!.year + 1900
        val expiresMonth = vehicle.insurance!!.expires!!.month + 1
        val expiresDate = vehicle.insurance!!.expires!!.date - 1
        val expiresvalue = (""+expiresDate.toString() + "/" +
                expiresMonth.toString() + "/" + expiresYear.toString() )
        textedit_profile_vehicle_insurance_expires.setText(expiresvalue)
    }

    private fun instantiateDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datepicker = activity!!.findViewById<EditText>(R.id.textedit_profile_vehicle_insurance_expires)

        datepicker.setOnClickListener() {
            val dpd = DatePickerDialog(
                activity,
                R.style.MySpinnerDatePickerStyle,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    var monthOfYear2 = monthOfYear +1
                    datepicker.setText("" + dayOfMonth + "/" + monthOfYear2 + "/" + year)
                },
                year,
                month,
                day
            )

            dpd.show()
        }

    }

    private fun instantiateSpinners() {
        insurers = viewModel.getInsurers().value!!
        Log.d("testpurp2", insurers.toString())
        val option = activity!!.findViewById<Spinner>(R.id.spinner_profile_vehicle_insurance_insurer)
        val adapter =
            ArrayAdapter(activity!!, android.R.layout.simple_spinner_item, insurers.map { insurer -> insurer.name })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        option.adapter = adapter

        if (vehicle!!.insurance != null) {
            val spinnerPosition = adapter.getPosition(vehicle!!.insurance!!.insurer!!.name)
            option.setSelection(spinnerPosition)
        }

        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                insurerName = adapter.getItem(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        };
    }

    fun addObject(item: Vehicle) {
        this.vehicle = item
    }

}
