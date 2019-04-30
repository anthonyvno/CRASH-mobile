package com.example.europeesaanrijdingsformulier.profile

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.utils.PrefManager
import com.example.europeesaanrijdingsformulier.utils.SpinnerManager
import kotlinx.android.synthetic.main.fragment_profile_vehicle_detail.*


class ProfileVehicleDetailFragment : Fragment() {

    private var vehicle: Vehicle? = null
    private lateinit var prefManager: PrefManager
    private var category: String = "Auto"
    private var country: String = "Belgium"
    private val spinnerManager = SpinnerManager()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prefManager = PrefManager(activity)

        return inflater.inflate(R.layout.fragment_profile_vehicle_detail, container, false)
    }

    override fun onStart() {
        super.onStart()
        if (vehicle != null) {
            fillInTextFields()
        }

        val optionCountries = spinnerManager.instantiateSpinner(activity!!,R.id.spinner_vehicle_detail_country,
            resources.getStringArray(R.array.countries_array))
        val option = spinnerManager.instantiateSpinner(activity!!,R.id.spinner_vehicle_detail_type, resources.getStringArray(R.array.vehicleCategory))
        val adapter = option.adapter as ArrayAdapter<String>
        val countriesAdapter = optionCountries.adapter as ArrayAdapter<String>
        if (vehicle?.type != null) {
            val spinnerPosition = adapter.getPosition(vehicle!!.type)
            option.setSelection(spinnerPosition)
        }
        if (vehicle?.country != null) {
            val spinnerPosition = countriesAdapter.getPosition(vehicle!!.country)
            optionCountries.setSelection(spinnerPosition)
        }

        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                category = adapter.getItem(position) as String
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        optionCountries.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                country = optionCountries.adapter.getItem(position) as String
            }
        }

        button_vehicle_detail_confirm.setOnClickListener {
            val brand = textedit_vehicle_detail_brand.text.toString()
            val model = textedit_vehicle_detail_model.text.toString()
            val licensePlate = textedit_vehicle_detail_licensePlate.text.toString()
            vehicle = if (vehicle == null) {
                if(prefManager.getVehicles()==null){
                    Vehicle(0, country, licensePlate, brand, model, category)

                } else {
                    Vehicle(prefManager.getVehicles()!!.size, country, licensePlate, brand, model, category)
                }
            } else {
                Vehicle(vehicle!!.id, country, licensePlate, brand, model, category,vehicle!!.insurance)
            }


            val fragment = ProfileVehicleInsuranceFragment()
            fragment.addObject(vehicle!!)
            this.fragmentManager!!.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right)
                .replace(R.id.container_main, fragment,"insurance")
                .addToBackStack("detail_to_insurance")
                .commit()
        }
    }

    private fun fillInTextFields() {
        textedit_vehicle_detail_brand.setText(vehicle!!.brand)
        textedit_vehicle_detail_licensePlate.setText(vehicle!!.licensePlate)
        textedit_vehicle_detail_model.setText(vehicle!!.model)
    }

    fun addObject(item: Vehicle) {
        this.vehicle = item
    }

    companion object {
    }
}
