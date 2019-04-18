package com.example.europeesaanrijdingsformulier.report

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.util.Patterns
import android.view.*
import android.widget.Toast
import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.profile.Profile
import com.example.europeesaanrijdingsformulier.utils.PrefManager
import com.example.europeesaanrijdingsformulier.utils.Validator
import kotlinx.android.synthetic.main.fragment_report_algemeen_a.*


class ReportAlgemeenAFragment : Fragment() {

    private lateinit var report: Report
    private var profile: Profile? = null
    private lateinit var prefManager: PrefManager
    private val validator = Validator()


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

        if (profile != null) {
            textedit_algemeen_a_firstname.setText(profile!!.firstName)
            textedit_algemeen_a_lastname.setText(profile!!.lastName)
            textedit_algemeen_a_email.setText(profile!!.email)

        }

        button_algemeen_a_confirm.setOnClickListener {
            if (isValidated()) {
                val firstName = this.textedit_algemeen_a_firstname.text.toString()
                val lastName = this.textedit_algemeen_a_lastname.text.toString()
                val email = this.textedit_algemeen_a_email.text.toString()
                var profiles: List<Profile>
                if (profile != null) {
                    profiles = listOf(Profile(1, firstName, lastName, email, profile?.license, profile?.vehicles))
                } else {
                    profiles = listOf(Profile(1, firstName, lastName, email))

                }

                report.profiles = profiles


                val reportLicenseAFragment = ReportLicenseAFragment()
                this.fragmentManager!!.beginTransaction()
                    .setCustomAnimations(
                        R.anim.enter_from_right,
                        R.anim.exit_to_left,
                        R.anim.enter_from_left,
                        R.anim.exit_to_right
                    )
                    .replace(R.id.container_main, reportLicenseAFragment)
                    .addToBackStack(null)
                    .commit()
                reportLicenseAFragment.addObject(report)

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
        if (!prefManager.getVehicles().isNullOrEmpty() && prefManager.getVehicles()?.first()?.insurance?.phoneAgency != "") {
            item.isVisible = true
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_belVerzekeraar -> {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:" + prefManager.getVehicles()?.first()?.insurance?.phoneAgency)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun isValidated(): Boolean {
        if (validator.isNotEmpty(this.textedit_algemeen_a_firstname.text.toString()) ||
            validator.isNotEmpty(this.textedit_algemeen_a_lastname.text.toString())
        ) {
            if (validator.isValidEmail(this.textedit_algemeen_a_email.text.toString())) {
                return true
            } else {
                this.textedit_algemeen_a_email.setError("Geen geldig e-mail adres")
            }
        } else Toast.makeText(
            activity,
            Html.fromHtml("<font color='#FF0000' ><b>" + "Voornaam of achternaam moet ingevuld zijn." + "</b></font>"),
            Toast.LENGTH_LONG
        ).show()

        return false
    }

}
