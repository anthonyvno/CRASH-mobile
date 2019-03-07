package com.example.europeesaanrijdingsformulier.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.profile.ProfileSummaryFragment
import com.example.europeesaanrijdingsformulier.report.ReportAlgemeenAFragment
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




    }

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

        button_home_startReport.setOnClickListener{
            this.fragmentManager!!.beginTransaction()
                .replace(R.id.container_main, ReportAlgemeenAFragment())
                .addToBackStack(null)
                .commit()
        }

        button_home_toProfile.setOnClickListener{
            this.fragmentManager!!.beginTransaction()
                .replace(R.id.container_main,ProfileSummaryFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onStop() {
        super.onStop()
        activity!!.actionBar?.show()
    }


}
