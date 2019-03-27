package com.example.europeesaanrijdingsformulier.report


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.anthonyvannoppen.androidproject.ui.HubViewModel

import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.fragments.HomeFragment
import com.example.europeesaanrijdingsformulier.utils.PrefManager
import com.github.gcacace.signaturepad.views.SignaturePad
import kotlinx.android.synthetic.main.fragment_report_confirmation.*





class ReportConfirmationFragment : Fragment() {

    private lateinit var viewModel: HubViewModel
    private lateinit var prefManager: PrefManager
    private lateinit var report: Report


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prefManager = PrefManager(activity)
        viewModel = ViewModelProviders.of(activity!!).get(HubViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_confirmation, container, false)
    }

    override fun onStart() {
        super.onStart()

        val signaturePad =  signature_pad
        signaturePad.setOnSignedListener(object : SignaturePad.OnSignedListener {

            override fun onStartSigning() {
                //Event triggered when the pad is touched
            }

            override fun onSigned() {
                //Event triggered when the pad is signed
            }

            override fun onClear() {
                //Event triggered when the pad is cleared
            }
        })

        button_report_confirmation_confirm.setOnClickListener{




            prefManager.saveReport(report)
            viewModel.postReport(report)
            val fragment = HomeFragment()
            this.fragmentManager!!.beginTransaction()
                .setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
                .replace(R.id.container_main, fragment)
                //.addToBackStack(null)
                .commit()

        }
    }

    fun addObject(item: Report) {
        this.report = item
    }


}
