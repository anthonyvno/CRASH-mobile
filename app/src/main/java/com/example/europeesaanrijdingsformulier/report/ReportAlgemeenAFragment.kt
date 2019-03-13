package com.example.europeesaanrijdingsformulier.report


import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.anthonyvannoppen.androidproject.ui.HubViewModel

import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.profile.License
import com.example.europeesaanrijdingsformulier.profile.Profile
import kotlinx.android.synthetic.main.fragment_report_algemeen_a.*


class   ReportAlgemeenAFragment : Fragment() {

    private lateinit var report: Report
    private lateinit var profile: Profile


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_algemeen_a, container, false)
    }

    override fun onStart() {
        super.onStart()

        if(profile != null){
            textedit_algemeen_a_firstname.setText(profile.firstName)
            textedit_algemeen_a_lastname.setText(profile.lastName)
            textedit_algemeen_a_email.setText(profile.email)

        }
        
        button_algemeen_a_confirm.setOnClickListener{
            val firstName =this.textedit_algemeen_a_firstname.text.toString()
            val lastName = this.textedit_algemeen_a_lastname.text.toString()
            val email = this.textedit_algemeen_a_email.text.toString()
            val profiles = listOf(Profile(1,firstName,lastName,email))

            report.profiles = profiles

            val reportStartBFragment = ReportStartBFragment()
            this.fragmentManager!!.beginTransaction()
                .replace(R.id.container_main, reportStartBFragment)
                .addToBackStack(null)
                .commit()
            reportStartBFragment.addObject(report)
        }
    }

    fun addReport(item: Report) {
        this.report = item
    }
    fun addProfile(item: Profile) {
        this.profile = item
    }


    /*
    companion object {

        fun newInstance(): ReportAlgemeenAFragment {
            val args = Bundle()
            //args.putSerializable("list", list)
            val fragment = ReportAlgemeenAFragment()
            fragment.arguments = args

            return fragment
        }
    }
    */
}
