package com.example.europeesaanrijdingsformulier

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.anthonyvannoppen.androidproject.ui.HubViewModel
import com.example.europeesaanrijdingsformulier.fragments.HomeFragment
import com.example.europeesaanrijdingsformulier.insurer.Insurer

import kotlinx.android.synthetic.main.activity_main.*
import java.util.logging.Logger

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel :  HubViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(my_toolbar)
        viewModel = ViewModelProviders.of(this).get(HubViewModel::class.java)

        supportFragmentManager.beginTransaction()
            .add(R.id.container_main, HomeFragment())
            .addToBackStack("main")
            .commit()

        //val insurer1 = viewModel.getInsurers().value!!.get(0).name
        //Log.d("sander", insurer1)
        viewModel.getInsurers().observe(this, Observer{
            it!!.forEach { i -> Log.d("sander",i.name+i.country+i.id )}
        })

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
}
