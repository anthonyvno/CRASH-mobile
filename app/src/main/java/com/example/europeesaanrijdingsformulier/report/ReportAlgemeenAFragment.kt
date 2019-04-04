package com.example.europeesaanrijdingsformulier.report
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Patterns
import android.view.*
import android.widget.Toast
import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.profile.Profile
import com.example.europeesaanrijdingsformulier.utils.PrefManager
import kotlinx.android.synthetic.main.fragment_report_algemeen_a.*


class   ReportAlgemeenAFragment : Fragment() {

    private lateinit var report: Report
    private var profile: Profile?=null
    private lateinit var prefManager: PrefManager



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        prefManager = PrefManager(activity)
        setHasOptionsMenu(true)


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_algemeen_a, container, false)
    }

    override fun onStart() {
        super.onStart()

        if(profile != null){
            textedit_algemeen_a_firstname.setText(profile!!.firstName)
            textedit_algemeen_a_lastname.setText(profile!!.lastName)
            textedit_algemeen_a_email.setText(profile!!.email)

        }
        
        button_algemeen_a_confirm.setOnClickListener{
            val firstName =this.textedit_algemeen_a_firstname.text.toString()
            val lastName = this.textedit_algemeen_a_lastname.text.toString()
            val email = this.textedit_algemeen_a_email.text.toString()
            var profiles: List<Profile>
            if(profile != null){
                profiles = listOf(Profile(1,firstName,lastName,email,profile?.license, profile?.vehicles))
            } else {
                profiles = listOf(Profile(1,firstName,lastName,email))

            }

            report.profiles = profiles

            if(email.isValidEmail()){
                val reportLicenseAFragment = ReportLicenseAFragment()
                this.fragmentManager!!.beginTransaction()
                    .replace(R.id.container_main, reportLicenseAFragment)
                    //.addToBackStack(null)
                    .commit()
                reportLicenseAFragment.addObject(report)
            } else{
                Toast.makeText(activity, "Message : "+ "Dit is geen geldig e-mailadres", Toast.LENGTH_SHORT).show()
            }


        }
    }

    fun addReport(item: Report) {
        this.report = item
    }
    fun addProfile(item: Profile) {
        this.profile = item
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

    fun String.isValidEmail(): Boolean
            = this.isNotEmpty() &&
            Patterns.EMAIL_ADDRESS.matcher(this).matches()

}
