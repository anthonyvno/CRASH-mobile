package com.example.europeesaanrijdingsformulier.fragments


import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.view.*
import android.widget.Toast
import com.example.anthonyvannoppen.androidproject.utils.inReport
import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.profile.ProfileSummaryFragment
import com.example.europeesaanrijdingsformulier.report.*
import com.example.europeesaanrijdingsformulier.utils.ConnectionManager
import kotlinx.android.synthetic.main.fragment_home.*
import com.example.europeesaanrijdingsformulier.MainActivity
import com.location.aravind.getlocation.GeoLocator






class HomeFragment : Fragment() {


    private val connectionManager = ConnectionManager()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bar = activity!! as AppCompatActivity
        bar.supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        bar.supportActionBar!!.setDisplayShowHomeEnabled(false)



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onStart() {
        super.onStart()
        //pdfWriterManager = PdfWriterManager()
        //pdfWriterManager.writePDF(prefManager.getReport()!!,activity)
        inReport = false
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(context!!,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
                //you code..
                //read_file()
            } else {
                //request permission
                ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE) , 0);
            }
        }

        button_home_startReport.setOnClickListener{
            if(connectionManager.checkConnection(context!!)){
                inReport = true
                this.fragmentManager!!.beginTransaction()
                    .setCustomAnimations(R.anim.abc_fade_in,R.anim.abc_fade_out,R.anim.abc_fade_in,R.anim.abc_fade_out)
                    .replace(R.id.container_main, ReportCrashInformation())
                    .addToBackStack("home_to_crashinformation")
                    .commit()
            } else {
                AlertDialog.Builder(activity)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Waarschuwing")
                    .setMessage("U heeft internetverbinding nodig om het aanrijdingsformulier te verzenden.\nToch doorgaan?")
                    .setPositiveButton(
                        "Ja",
                        DialogInterface.OnClickListener { dialog, which ->
                            inReport = true
                            this.fragmentManager!!.beginTransaction()
                                .setCustomAnimations(R.anim.abc_fade_in,R.anim.abc_fade_out,R.anim.abc_fade_in,R.anim.abc_fade_out)
                                .replace(R.id.container_main, ReportCrashInformation())
                                .addToBackStack("home_to_crashinformation")
                                .commit()
                        })

                    .setNegativeButton("Nee", null)
                    .show()
            }

        }

        button_home_toProfile.setOnClickListener{
            this.fragmentManager!!.beginTransaction()
                .setCustomAnimations(R.anim.abc_fade_in,R.anim.abc_fade_out,R.anim.abc_fade_in,R.anim.abc_fade_out)
                .replace(R.id.container_main,ProfileSummaryFragment())
                .addToBackStack("home_to_profileSummary")
                .commit()
        }

    }




    override fun onStop() {
        super.onStop()
        activity!!.actionBar?.show()
    }


}