package com.example.europeesaanrijdingsformulier.report


import android.app.AlertDialog
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.europeesaanrijdingsformulier.ui.HubViewModel
import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.fragments.HomeFragment
import com.example.europeesaanrijdingsformulier.utils.ConnectionManager
import com.example.europeesaanrijdingsformulier.utils.PrefManager
import com.github.gcacace.signaturepad.views.SignaturePad
import kotlinx.android.synthetic.main.fragment_report_confirmation.*
import java.io.ByteArrayOutputStream


class ReportConfirmationFragment : Fragment() {

    private lateinit var viewModel: HubViewModel
    private lateinit var prefManager: PrefManager
    private val connectionManager = ConnectionManager()
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

        val signaturePadA = signature_pad_a
        val signaturePadB = signature_pad_b

        signaturePadB.setOnSignedListener(object : SignaturePad.OnSignedListener {

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

        button_report_confirmation_delete_a.setOnClickListener{
            signaturePadA.clear()
        }
        button_report_confirmation_delete_b.setOnClickListener{
            signaturePadB.clear()
        }
        button_report_confirmation_confirm.setOnClickListener{
            if(connectionManager.checkConnection(activity!!)){
                val bitmapA = signaturePadA.signatureBitmap
                val streamA = ByteArrayOutputStream()
                bitmapA.compress(Bitmap.CompressFormat.PNG,100,streamA)
                val bytearrayA = streamA.toByteArray()
                val encodedA = Base64.encodeToString(bytearrayA, Base64.DEFAULT)

                val bitmapB = signaturePadB.signatureBitmap
                val streamB = ByteArrayOutputStream()
                bitmapB.compress(Bitmap.CompressFormat.PNG,100,streamB)
                val bytearrayB = streamB.toByteArray()
                val encodedB = Base64.encodeToString(bytearrayB, Base64.DEFAULT)

                report.signatures = arrayOf(encodedA,encodedB)

                println("pdf: " + report.pdfReport)
                println("sketch: " + report.sketch)


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
            } else {
                AlertDialog.Builder(activity)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Waarschuwing")
                    .setMessage("U heeft internetverbinding nodig om het aanrijdingsformulier te verzenden.")
                    .setNegativeButton("Ok", null)
                    .show()
            }



        }
    }

    fun addObject(item: Report) {
        this.report = item
    }


}
