package com.example.europeesaanrijdingsformulier

import android.app.AlertDialog
import android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import android.arch.lifecycle.ViewModelProviders
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.europeesaanrijdingsformulier.fragments.HomeFragment
import com.example.europeesaanrijdingsformulier.fragments.ReportListFragment
import com.example.europeesaanrijdingsformulier.insurer.Insurer
import com.example.europeesaanrijdingsformulier.ui.HubViewModel
import com.example.europeesaanrijdingsformulier.utils.ConnectionManager
import com.example.europeesaanrijdingsformulier.utils.PrefManager
import com.example.europeesaanrijdingsformulier.utils.inReport
import kotlinx.android.synthetic.main.activity_main.*




class MainActivity : AppCompatActivity() {

    private lateinit var prefManager: PrefManager
    private lateinit var viewModel: HubViewModel
    val connectionManager = ConnectionManager()
    private lateinit var mConnReceiver: BroadcastReceiver


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)
        setSupportActionBar(my_toolbar)
        prefManager = PrefManager(this)

        viewModel = ViewModelProviders.of(this).get(HubViewModel::class.java)


        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        /*
        val titleId = resources.getIdentifier(
            "action_bar_title", "id",
            "android"
        )
        val yourTextView = findViewById<View>(titleId) as TextView
        yourTextView.fontFeatureSettings(ResourcesCompat.getFont(this,R.font.magra))
*/
        //clear local

           val sharedPref = getSharedPreferences(R.string.preferences_profile.toString(), Context.MODE_PRIVATE)
             var editor = sharedPref.edit()
             editor.clear().apply()

        mConnReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (connectionManager.checkConnection(context)) {
                    if(inReport){
                        Toast.makeText(applicationContext, "Verbonden met internet", Toast.LENGTH_LONG).show()
                    }
                } else {
                    if (inReport) {
                        Toast.makeText(applicationContext, "Geen internetverbinding!", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        supportFragmentManager.beginTransaction()
            .add(R.id.container_main, HomeFragment())
            .addToBackStack("main")
            .commit()

    }

    override fun onResume() {
        super.onResume()
        this.registerReceiver(this.mConnReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        if (connectionManager.checkConnection(this)) {
            prefManager.saveInsurers(viewModel.getNogInsurers().blockingFirst() as MutableList<Insurer>)
            println(prefManager.getInsurers())
        }
    }
    override fun onPause() {
        super.onPause()
        this.unregisterReceiver(this.mConnReceiver)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_formulieren -> {
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        R.anim.abc_fade_in,
                        R.anim.abc_fade_out,
                        R.anim.abc_fade_in,
                        R.anim.abc_fade_out
                    )
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
        if (fm.findFragmentByTag("summary") != null && fm.findFragmentByTag("summary")!!.isVisible) {
            fm.popBackStack("home_to_profileSummary", POP_BACK_STACK_INCLUSIVE)
        } else
            if (fm.findFragmentByTag("insurance") != null && fm.findFragmentByTag("insurance")!!.isVisible) {
                fm.popBackStack("list_to_detail", POP_BACK_STACK_INCLUSIVE)
            } else {
                if (fm.findFragmentByTag("startB") != null && fm.findFragmentByTag("startB")!!.isVisible) {
                    AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Afsluiten formulier")
                        .setMessage("Wilt u terug gaan naar het beginscherm? Ingevulde gegevens gaan verloren.")
                        .setPositiveButton(
                            "Ja"
                        ) { dialog, which -> fm.popBackStack("home_to_crashinformation", POP_BACK_STACK_INCLUSIVE) }
                        .setNegativeButton("Nee", null)
                        .show()
                } else {
                    if (fm.findFragmentByTag("circumstances") != null && fm.findFragmentByTag("circumstances")!!.isVisible) {
                        AlertDialog.Builder(this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Afsluiten formulier")
                            .setMessage("Wilt u terug gaan naar het beginscherm? Ingevulde gegevens gaan verloren.")
                            .setPositiveButton(
                                "Ja"
                            ) { dialog, which -> fm.popBackStack("home_to_crashinformation", POP_BACK_STACK_INCLUSIVE) }
                            .setNegativeButton("Nee", null)
                            .show()
                    } else {
                        super.onBackPressed()
                    }
                }


            }
    }
}
