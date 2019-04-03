package com.example.europeesaanrijdingsformulier.fragments


import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.profile.ProfileSummaryFragment
import com.example.europeesaanrijdingsformulier.profile.Vehicle
import com.example.europeesaanrijdingsformulier.report.Report
import com.example.europeesaanrijdingsformulier.report.ReportAlgemeenAFragment
import com.example.europeesaanrijdingsformulier.report.ReportCrashInformation
import com.example.europeesaanrijdingsformulier.report.ReportOverviewFragment
import com.example.europeesaanrijdingsformulier.utils.PdfWriterManager
import com.example.europeesaanrijdingsformulier.utils.PrefManager
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {



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
            this.fragmentManager!!.beginTransaction()
                .setCustomAnimations(R.anim.abc_fade_in,R.anim.abc_fade_out,R.anim.abc_fade_in,R.anim.abc_fade_out)
                .replace(R.id.container_main, ReportCrashInformation())
                .addToBackStack(null)
                .commit()
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