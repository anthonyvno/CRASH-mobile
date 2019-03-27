package com.example.europeesaanrijdingsformulier.report


import android.Manifest
import android.app.DatePickerDialog
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import com.example.anthonyvannoppen.androidproject.ui.HubViewModel

import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.fragments.HomeFragment
import com.example.europeesaanrijdingsformulier.insurer.Insurer
import com.example.europeesaanrijdingsformulier.profile.Insurance
import com.example.europeesaanrijdingsformulier.utils.DatePickerManager
import com.example.europeesaanrijdingsformulier.utils.PrefManager
import com.example.europeesaanrijdingsformulier.utils.QRManager
import com.example.europeesaanrijdingsformulier.utils.SpinnerManager
import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfContentByte
import com.itextpdf.text.pdf.PdfWriter
import kotlinx.android.synthetic.main.fragment_report_vehicle_insurance_b.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*


class ReportVehicleInsuranceBFragment : Fragment() {

    private lateinit var report: Report
    private lateinit var insurerName: String
    private lateinit var insurers: List<Insurer>
    private lateinit var viewModel: HubViewModel
    private lateinit var prefManager: PrefManager
    private val spinnerManager = SpinnerManager()
    private val datePickerManager = DatePickerManager()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //prefManager = PrefManager(activity)
        viewModel = ViewModelProviders.of(activity!!).get(HubViewModel::class.java)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_vehicle_insurance_b, container, false)
    }

    override fun onStart() {
        super.onStart()
        insurers = viewModel.getInsurers().value!!
        val option = spinnerManager.instantiateSpinner(activity!!, R.id.spinner_report_vehicle_insurance_b_insurer,
            insurers.map { insurer -> insurer!!.name }.toTypedArray()
        )

        val adapter = option.adapter as ArrayAdapter<String>

        if (report.profiles.last().vehicles?.first()?.insurance?.insurer != null) {
            val spinnerPosition =
                adapter.getPosition(report.profiles.last().vehicles?.first()?.insurance!!.insurer!!.name)
            option.setSelection(spinnerPosition)
        }

        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                insurerName = adapter.getItem(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
        datePickerManager.instantiateDatePicker(activity!!, R.id.textedit_report_vehicle_insurance_b_expires)
        if (report.profiles.last().vehicles?.first()?.insurance != null) {
            fillInTextFields()
        }
        button_report_vehicle_insurance_b_confirm.setOnClickListener {
            val date = textedit_report_vehicle_insurance_b_expires.text.toString()
            val dateSplit = date.split("/")
            val dateExpires = Date(
                dateSplit[2].toInt() - 1900, dateSplit[1].toInt() - 1, dateSplit[0].toInt() + 1
            )
            val insurer4 = insurers.find { insurer -> insurer.name == insurerName }
            var insurance = Insurance(
                1,
                textedit_report_vehicle_insurance_b_insuranceNumber.text.toString(),
                textedit_report_vehicle_insurance_b_greenCard.text.toString(),
                textedit_report_vehicle_insurance_b_email.text.toString(),
                dateExpires,
                textedit_report_vehicle_insurance_b_phone.text.toString(),
                insurer4
            )

            report.profiles.last().vehicles?.first()?.insurance = insurance

            //prefManager.saveReport(report)
            //viewModel.postReport(report)
            generatePDF()
            val fragment = ReportConfirmationFragment()
            fragment.addObject(report)
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

    private fun fillInTextFields() {
        textedit_report_vehicle_insurance_b_email.setText(report.profiles.last().vehicles?.first()?.insurance!!.emailAgency)
        textedit_report_vehicle_insurance_b_phone.setText(report.profiles.last().vehicles?.first()?.insurance!!.phoneAgency)
        textedit_report_vehicle_insurance_b_greenCard.setText(report.profiles.last().vehicles?.first()?.insurance!!.greenCardNumber)
        textedit_report_vehicle_insurance_b_insuranceNumber.setText(report.profiles.last().vehicles?.first()?.insurance!!.insuranceNumber)
        if (report.profiles.last().vehicles?.first()?.insurance!!.expires != null) {
            val expiresYear = report.profiles.last().vehicles?.first()?.insurance!!.expires!!.year + 1900
            val expiresMonth = report.profiles.last().vehicles?.first()?.insurance!!.expires!!.month + 1
            val expiresDate = report.profiles.last().vehicles?.first()?.insurance!!.expires!!.date - 1
            val expiresvalue = ("" + expiresDate.toString() + "/" +
                    expiresMonth.toString() + "/" + expiresYear.toString())
            textedit_report_vehicle_insurance_b_expires.setText(expiresvalue)
        }

    }

    fun generatePDF() {

        val pdfFolder = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS
            ), "pdfdemo"
        )
        if (!pdfFolder.exists()) {
            pdfFolder.mkdirs()
        }
        val file = File(pdfFolder, "aanrijdingsformulier.pdf")


        var document = Document(PageSize.A4,0F,0F,0F,0F)
        val writer = PdfWriter.getInstance(document, FileOutputStream(file))
        document.open()
        val canvas: PdfContentByte
        canvas = writer.directContentUnder

        val d = getResources().getDrawable(R.drawable.aanrijdingsform)
        val bitDw = (d as BitmapDrawable)
        val bmp = bitDw.getBitmap()
        val stream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val image = Image.getInstance(stream.toByteArray())


        //val image = Image.getInstance("http://www.quiviveverzekeringen.be/image/735-0/cb04af7aa673baea44a439a2622a151629.jpg")
        //image.scaleAbsolute(PageSize.A4.rotate())
        image.setAbsolutePosition(0F,0F)
        canvas.addImage(image)
        //document.add(Paragraph("Hallo"))
        document.close()


    }

    fun addObject(item: Report) {
        this.report = item
    }


}
