package com.example.europeesaanrijdingsformulier

import android.app.Activity
import android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity;
import android.text.Html
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.anthonyvannoppen.androidproject.ui.HubViewModel
import com.example.europeesaanrijdingsformulier.fragments.HomeFragment
import com.example.europeesaanrijdingsformulier.fragments.ReportListFragment
import com.example.europeesaanrijdingsformulier.insurer.Insurer
import com.example.europeesaanrijdingsformulier.profile.*
import com.example.europeesaanrijdingsformulier.report.Report
import com.example.europeesaanrijdingsformulier.report.ReportAlgemeenAFragment
import com.example.europeesaanrijdingsformulier.utils.PrefManager
import com.example.europeesaanrijdingsformulier.utils.QRManager
import com.google.gson.Gson
import com.google.zxing.integration.android.IntentIntegrator

import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.util.logging.Logger

class MainActivity : AppCompatActivity() {

    private lateinit var prefManager: PrefManager
    private val qrManager = QRManager()
    private lateinit var viewModel: HubViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(my_toolbar)
        prefManager = PrefManager(this)

        viewModel = ViewModelProviders.of(this).get(HubViewModel::class.java)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        //clear local

           val sharedPref = getSharedPreferences(R.string.preferences_profile.toString(), Context.MODE_PRIVATE)
             var editor = sharedPref.edit()
             editor.clear().apply()


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
            R.id.action_formulieren -> {
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.abc_fade_in,R.anim.abc_fade_out,R.anim.abc_fade_in,R.anim.abc_fade_out)
                    .replace(R.id.container_main, ReportListFragment())
                    .addToBackStack(null)
                    .commit()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        val fm = supportFragmentManager
        if(fm.findFragmentByTag("summary")!=null && fm.findFragmentByTag("summary")!!.isVisible()){
            fm.popBackStack("home_to_profileSummary",POP_BACK_STACK_INCLUSIVE)
        } else
        if(fm.findFragmentByTag("insurance")!=null && fm.findFragmentByTag("insurance")!!.isVisible() ){
            fm.popBackStack("list_to_detail", POP_BACK_STACK_INCLUSIVE)
        } else {
            super.onBackPressed()
        }
    }





}
