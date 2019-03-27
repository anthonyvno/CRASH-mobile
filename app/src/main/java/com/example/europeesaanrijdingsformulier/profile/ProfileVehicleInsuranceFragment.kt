package com.example.europeesaanrijdingsformulier.profile

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.anthonyvannoppen.androidproject.ui.HubViewModel
import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.insurer.Insurer
import com.example.europeesaanrijdingsformulier.utils.DatePickerManager
import com.example.europeesaanrijdingsformulier.utils.PrefManager
import com.example.europeesaanrijdingsformulier.utils.SpinnerManager
import kotlinx.android.synthetic.main.fragment_profile_vehicle_insurance.*
import java.util.*

class ProfileVehicleInsuranceFragment : Fragment() {

    private lateinit var insurers: List<Insurer>
    private lateinit var prefManager: PrefManager
    private lateinit var vehicle: Vehicle
    private lateinit var insurerName: String
    private lateinit var viewModel: HubViewModel
    private val spinnerManager = SpinnerManager()
    private val datePickerManager = DatePickerManager()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prefManager = PrefManager(activity)
        viewModel = ViewModelProviders.of(activity!!).get(HubViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_vehicle_insurance, container, false)
    }


    override fun onStart() {
        super.onStart()
        datePickerManager.instantiateDatePicker(activity!!, R.id.textedit_profile_vehicle_insurance_expires)

        insurers = viewModel.getInsurers().value!!
        val option = spinnerManager.instantiateSpinner(activity!!, R.id.spinner_profile_vehicle_insurance_insurer,
            insurers.map { insurer -> insurer.name }.toTypedArray()
        )

        val adapter = option.adapter as ArrayAdapter<String>

        if (vehicle!!.insurance!!.insurer != null) {
            val spinnerPosition = adapter.getPosition(vehicle!!.insurance!!.insurer!!.name)
            option.setSelection(spinnerPosition)
        }

        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                insurerName = adapter.getItem(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
        if (vehicle!!.insurance != null) {
            fillInTextFields()
        }
        button_profile_vehicle_insurance_confirm.setOnClickListener {
            val date = textedit_profile_vehicle_insurance_expires.text.toString()
            val dateSplit = date.split("/")
            val dateExpires = Date(
                dateSplit[2].toInt() - 1900, dateSplit[1].toInt() - 1, dateSplit[0].toInt() + 1
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

            val profile = prefManager.getProfile()

            vehicle.insurance = insurance
            var vehicles = prefManager.getVehicles()
            if (vehicles != null) {
                var comparevehicle = vehicles.find { it.id == vehicle.id }
                if (comparevehicle == null) {
                    vehicles.add(vehicle)
                } else {
                    vehicles.remove(comparevehicle)
                    vehicles.add(vehicle)
                }
            } else {
                vehicles = mutableListOf<Vehicle>(vehicle)
            }

            prefManager.saveVehicles(vehicles)
            profile!!.vehicles = vehicles
            prefManager.saveProfile(profile)

            this.fragmentManager!!.beginTransaction()
                .setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out, R.anim.abc_fade_in, R.anim.abc_fade_out)
                .replace(R.id.container_main, ProfileSummaryFragment(), "summary")
                //.addToBackStack(null)
                .commit()
        }


    }

    private fun fillInTextFields() {
        textedit_profile_vehicle_insurance_email.setText(vehicle!!.insurance!!.emailAgency)
        textedit_profile_vehicle_insurance_phone.setText(vehicle!!.insurance!!.phoneAgency)
        textedit_profile_vehicle_insurance_greenCard.setText(vehicle!!.insurance!!.greenCardNumber)
        textedit_profile_vehicle_insurance_insuranceNumber.setText(vehicle!!.insurance!!.insuranceNumber)
        if (vehicle.insurance!!.expires != null) {
            val expiresYear = vehicle.insurance!!.expires!!.year + 1900
            val expiresMonth = vehicle.insurance!!.expires!!.month + 1
            val expiresDate = vehicle.insurance!!.expires!!.date - 1
            val expiresvalue = ("" + expiresDate.toString() + "/" +
                    expiresMonth.toString() + "/" + expiresYear.toString())
            textedit_profile_vehicle_insurance_expires.setText(expiresvalue)
        }

    }

    fun addObject(item: Vehicle) {
        this.vehicle = item
    }

}
