package com.example.europeesaanrijdingsformulier.report


import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.anthonyvannoppen.androidproject.ui.HubViewModel

import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.fragments.HomeFragment
import com.example.europeesaanrijdingsformulier.profile.Profile
import kotlinx.android.synthetic.main.fragment_report_algemeen_a.*
import java.time.LocalDate
import java.util.*


class   ReportAlgemeenAFragment : Fragment() {

    private lateinit var report: Report
    private lateinit var viewModel: HubViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProviders.of(activity!!).get(HubViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_algemeen_a, container, false)
    }

    @SuppressLint("NewApi")
    override fun onStart() {
        super.onStart()

        button_algemeen_a_confirm.setOnClickListener{
            val firstName =this.edit_algemeen_a_firstname.text.toString()
            val lastName = this.edit_algemeen_a_lastname.text.toString()
            val email = this.edit_algemeen_a_email.text.toString()



            val profiel = viewModel.postProfile(Profile(1,firstName,lastName,email)).blockingFirst()
            val profiles = listOf(profiel)

            report = Report(1,profiles)

            viewModel.postReport(report)

            this.fragmentManager!!.beginTransaction()
                .replace(R.id.container_main, HomeFragment())
                .addToBackStack(null)
                .commit()
        }
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
