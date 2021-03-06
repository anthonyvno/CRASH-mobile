package com.example.europeesaanrijdingsformulier.report


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.profile.Vehicle
import com.example.europeesaanrijdingsformulier.utils.PrefManager
import com.example.europeesaanrijdingsformulier.utils.SpinnerManager
import kotlinx.android.synthetic.main.fragment_report_vehicle_detail_a.*


class ReportVehicleDetailAFragment : Fragment() {

    private lateinit var report: Report
    private var category: String = "Auto"
    private var country: String = "Belgium"
    private val spinnerManager = SpinnerManager()
    private lateinit var prefManager: PrefManager



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prefManager = PrefManager(activity)
        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_vehicle_detail_a, container, false)
    }

    override fun onStart() {
        super.onStart()
        val optionCountries = spinnerManager.instantiateSpinner(activity!!,R.id.spinner_report_vehicle_detail_a_country,
            resources.getStringArray(R.array.countries_array))
        val option = spinnerManager.instantiateSpinner(activity!!,R.id.spinner_report_vehicle_detail_a_type, resources.getStringArray(R.array.vehicleCategory))
        val adapter = option.adapter as ArrayAdapter<String>
        val countriesAdapter = optionCountries.adapter as ArrayAdapter<String>
        if (report.profiles.first().vehicles!!.isNotEmpty()  && report.profiles.first().vehicles?.first()?.type != null) {
            val spinnerPosition = adapter.getPosition(report.profiles.first().vehicles?.first()!!.type)
            option.setSelection(spinnerPosition)
        }
        if (report.profiles.first().vehicles!!.isNotEmpty()  && report.profiles.first().vehicles?.first()?.country != null) {
            val spinnerPosition = countriesAdapter.getPosition(report.profiles.first().vehicles?.first()!!.country)
            optionCountries.setSelection(spinnerPosition)
        }

        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                category = adapter.getItem(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        optionCountries.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                country = countriesAdapter.getItem(position)
            }
        }

        if (report.profiles.first().vehicles!!.isNotEmpty()  && report.profiles.first().vehicles?.first() != null) {
            fillInTextFields()
        }

        button_report_vehicle_detail_a_confirm.setOnClickListener{

             val brand = textedit_report_vehicle_detail_a_brand.text.toString()
            val model = textedit_report_vehicle_detail_a_model.text.toString()
            val licensePlate = textedit_report_vehicle_detail_a_licensePlate.text.toString()

            if(report.profiles.first().vehicles!!.isNotEmpty()){
                report.profiles.first().vehicles!!.first().licensePlate = licensePlate
                report.profiles.first().vehicles!!.first().brand = brand
                report.profiles.first().vehicles!!.first().model = model
                report.profiles.first().vehicles!!.first().type = category
                report.profiles.first().vehicles!!.first().country = country
            } else {
                report.profiles.first().vehicles = listOf(Vehicle(0,country,licensePlate,brand,model,category))
            }

            val fragment = ReportVehicleInsuranceAFragment()
            fragment.addObject(report)
            this.fragmentManager!!.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right)
                .replace(R.id.container_main, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun fillInTextFields() {
        textedit_report_vehicle_detail_a_brand.setText(report.profiles.first().vehicles?.first()?.brand)
        textedit_report_vehicle_detail_a_licensePlate.setText(report.profiles.first().vehicles?.first()?.licensePlate)
        textedit_report_vehicle_detail_a_model.setText(report.profiles.first().vehicles?.first()?.model)
    }



    fun addObject(item: Report) {
        this.report = item
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        //menu?.clear()
        //inflater!!.inflate(R.menu.menu_main,menu)
        super.onCreateOptionsMenu(menu, inflater)

        val item = menu!!.findItem(R.id.action_belVerzekeraar)
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
