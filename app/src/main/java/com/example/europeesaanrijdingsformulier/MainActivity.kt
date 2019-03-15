package com.example.europeesaanrijdingsformulier

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.text.Html
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.anthonyvannoppen.androidproject.ui.HubViewModel
import com.example.europeesaanrijdingsformulier.fragments.HomeFragment
import com.example.europeesaanrijdingsformulier.insurer.Insurer
import com.example.europeesaanrijdingsformulier.profile.Insurance
import com.example.europeesaanrijdingsformulier.profile.Profile
import com.example.europeesaanrijdingsformulier.profile.Vehicle
import com.example.europeesaanrijdingsformulier.report.Report
import com.example.europeesaanrijdingsformulier.report.ReportAlgemeenAFragment
import com.example.europeesaanrijdingsformulier.utils.PrefManager
import com.google.gson.Gson
import com.google.zxing.integration.android.IntentIntegrator

import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.util.logging.Logger

class MainActivity : AppCompatActivity() {

    private lateinit var prefManager: PrefManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(my_toolbar)
        prefManager = PrefManager(this)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        //clear local

        /*   val sharedPref = getSharedPreferences(R.string.preferences_profile.toString(), Context.MODE_PRIVATE)
             var editor = sharedPref.edit()
             editor.clear().apply()
     */

        supportFragmentManager.beginTransaction()
            .add(R.id.container_main, HomeFragment())
            .addToBackStack("main")
            .commit()


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)



        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_formulieren -> true
            else -> super.onOptionsItemSelected(item)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            Log.d("qr result2:", result.contents)
            if (result != null) {
                if (result.contents == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
                } else {
                    val fragment = ReportAlgemeenAFragment()
                    //val gson = Gson()
                    if (result.contents.startsWith("{\"version\"")) {
                        val json = JSONObject(result.contents)
                        val insured = json.getJSONObject("insured")
                        val insurance = json.getJSONObject("Insurer")
                        val vehicle = json.getJSONObject("vehicle")
                        //Log.d("insured",insured.toString())
                        val keys = insured.keys()
                        val keys2 = insurance.keys()
                        val keys3 = vehicle.keys()

                        var firstname = ""
                        var lastname = ""

                        var contractnumber = ""
                        var emailAgency = ""

                        var brand = ""
                        var licensePlate = ""
                        var country = ""


                        while (keys.hasNext()) {
                            val key = keys.next() as String
                            Log.d("keys", keys.next())
                            firstname = insured.optString("firstname")

                            lastname = insured.optString("lastname")

                        }
                        while (keys2.hasNext()) {
                            keys2.next()
                            emailAgency = insurance.optString("representativeEmail")
                            contractnumber = insurance.optString("contractNumber")
                        }
                        while(keys3.hasNext()){
                            keys3.next()
                            brand = vehicle.optString("brand")
                            licensePlate = vehicle.optString("licensePlate")
                            country = vehicle.optString("country")
                        }

                        fragment.addProfile(Profile(1, firstname, lastname, "",null,
                            listOf(Vehicle(1,country,licensePlate,brand,"","",
                            Insurance(1,contractnumber,"",emailAgency,null,"")
                        ))))
                        fragment.addReport(prefManager.getReport()!!)
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container_main, fragment)
                            .addToBackStack(null)
                            .commitAllowingStateLoss()
                    } else {
                        Toast.makeText(this, Html.fromHtml("<font color='#FF0000' ><b>" + "Geen geldige QR-code" + "</b></font>") , Toast.LENGTH_LONG).show()

                    }
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
