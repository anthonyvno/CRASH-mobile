package com.example.europeesaanrijdingsformulier.profile
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.utils.DatePickerManager
import com.example.europeesaanrijdingsformulier.utils.PrefManager
import com.example.europeesaanrijdingsformulier.utils.SpinnerManager
import kotlinx.android.synthetic.main.fragment_profile_license.*


class ProfileLicenseFragment : Fragment() {

    private lateinit var license: License
    private var category : String = ""
    private var licenseInput: License? = null
    private lateinit var prefManager: PrefManager
    private val spinnerManager = SpinnerManager()
    private val datePickerManager = DatePickerManager()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prefManager = PrefManager(activity)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_license, container, false)
    }

    override fun onStart() {
        super.onStart()
        datePickerManager.instantiateDatePicker(activity!!,R.id.textedit_profile_license_expires)

        val option = spinnerManager.instantiateSpinner(activity!!,R.id.spinner_profile_license_category, resources.getStringArray(R.array.licenseCategory))
        val adapter = option.adapter as ArrayAdapter<String>
        licenseInput = prefManager.getLicense()
        if(licenseInput?.category != null){
            val spinnerPosition = adapter.getPosition(licenseInput!!.category)
            option.setSelection(spinnerPosition)
        }

        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                category = adapter.getItem(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        if(licenseInput != null){
            fillInTextFields()
        }

        button_profile_license_confirm.setOnClickListener{
            val expires = textedit_profile_license_expires.text.toString()
            val licenseNumber = textedit_profile_license_licenseNumber.text.toString()


            val license2 : License
            license = if(licenseInput == null) {
                License(id,category,licenseNumber,expires)
            } else{
                License(licenseInput!!.id,category,licenseNumber,expires)
            }

            prefManager.saveLicense(license)


            val profile = prefManager.getProfile()
            profile!!.license = license


            prefManager.saveProfile(profile)

            this.fragmentManager!!.beginTransaction()
                .setCustomAnimations(R.anim.abc_fade_in,R.anim.abc_fade_out,R.anim.abc_fade_in,R.anim.abc_fade_out)
                .replace(com.example.europeesaanrijdingsformulier.R.id.container_main, ProfileSummaryFragment(),"summary")
                //.addToBackStack(null)
                .commit()
        }
    }

    private fun fillInTextFields() {
        textedit_profile_license_expires.setText(licenseInput!!.expires)
        textedit_profile_license_licenseNumber.setText(licenseInput!!.licenseNumber)
    }

}
