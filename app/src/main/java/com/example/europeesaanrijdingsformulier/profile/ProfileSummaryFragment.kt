package com.example.europeesaanrijdingsformulier.profile


import android.app.Activity
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.utils.PrefManager
import com.example.europeesaanrijdingsformulier.utils.QRManager
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.fragment_profile_summary.*


class ProfileSummaryFragment : Fragment() {

    private lateinit var prefManager: PrefManager
    private val gson = Gson()
    private val qrManager = QRManager()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prefManager = PrefManager(activity)
        val bar = activity!! as AppCompatActivity
        bar.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        bar.supportActionBar!!.setDisplayShowHomeEnabled(true)
        return inflater.inflate(R.layout.fragment_profile_summary, container, false)
    }

    override fun onStart() {
        super.onStart()
        navigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_scanQr -> {
                    val scanner = IntentIntegrator.forSupportFragment(this).initiateScan()
                    true
                }
                R.id.navigation_myQR -> {

                    val inflater = activity!!.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater?
                    val popupView = inflater!!.inflate(R.layout.popup_profile_qr, null)
                    val imageView = ImageView(activity)
                    val linearLayout = LinearLayout(activity)
                    val params = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                    )
                    linearLayout.orientation = LinearLayout.VERTICAL
                    imageView.setImageBitmap(generateQr())
                    linearLayout.addView(imageView, params)
                    val width = LinearLayout.LayoutParams.MATCH_PARENT
                    val height = LinearLayout.LayoutParams.MATCH_PARENT
                    val focusable = true // lets taps outside the popup also dismiss it
                    val popupWindow = PopupWindow(popupView, width, height, focusable)
                    popupWindow.contentView = linearLayout

                    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

                    linearLayout.setOnTouchListener { v, event ->
                        popupWindow.dismiss()
                        true
                    }
                    true
                }
            }
            false
        }

        val sharedPref = activity?.getSharedPreferences(R.string.preferences_profile.toString(), Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPref!!.getString("My_Profile", "")
        val json2 = sharedPref.getString("My_License", "")
        val profile = gson.fromJson(json, Profile::class.java)
        val license = gson.fromJson(json2, License::class.java)
        if (profile != null) {
            text_profile_summary_personal.text = profile.firstName + " " + profile.lastName
        } else {
            cardview2_profile_summary.visibility = View.INVISIBLE
            cardview3_profile_summary.visibility = View.INVISIBLE
        }

        if (license != null)
            text_profile_summary_license.text = license.category + " " + license.licenseNumber


        cardview1_profile_summary.setOnClickListener {

            this.fragmentManager!!.beginTransaction()
                .setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
                .replace(R.id.container_main, ProfileInfoFragment())
                .addToBackStack(null)
                .commit()
        }

        cardview2_profile_summary.setOnClickListener {

            this.fragmentManager!!.beginTransaction()
                .setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
                .replace(R.id.container_main, ProfileLicenseFragment())
                .addToBackStack(null)
                .commit()
        }

        cardview3_profile_summary.setOnClickListener {
            this.fragmentManager!!.beginTransaction()
                .setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out, R.anim.abc_fade_in, R.anim.abc_fade_out)
                .replace(com.example.europeesaanrijdingsformulier.R.id.container_main, ProfileVehicleListFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            Log.d("qr update profile:", result.contents)
            if (result != null) {
                if (result.contents == null) {
                    Toast.makeText(activity, "Cancelled", Toast.LENGTH_LONG).show()
                } else {
                    if (result.contents.startsWith("{\"version\"")) {
                        val profile = qrManager.handleGreenCardScan(result.contents)
                        prefManager.saveProfile(profile)
                        if(prefManager.getLicense() != null){
                            profile.license = prefManager.getLicense()
                            prefManager.saveProfile(profile)
                        }
                        val vehicles = prefManager.getVehicles()
                        if (vehicles != null) {
                            vehicles.add(profile.vehicles!!.first())
                            profile.vehicles = vehicles
                            prefManager.saveProfile(profile)
                            prefManager.saveVehicles(vehicles)
                        } else {
                            if (profile.vehicles != null) {
                                prefManager.saveVehicles(mutableListOf(profile.vehicles!!.first()))
                            }
                        }
                        fragmentManager!!.beginTransaction()
                            .detach(this)
                            .attach(this)
                            .commitAllowingStateLoss()
                    } else if (result.contents.startsWith("{\"email\"")) {
                        println(result.contents)
                        val profile = qrManager.handleProfileScan(result.contents)
                        prefManager.saveProfile(profile)
                        if (profile.license != null) {
                            prefManager.saveLicense(profile.license!!)
                        } else {
                            if(prefManager.getLicense() != null){
                                profile.license = prefManager.getLicense()
                                prefManager.saveProfile(profile)
                            }
                        }
                        val vehicles = prefManager.getVehicles()
                        if (vehicles != null) {
                            vehicles.add(profile.vehicles!!.first())
                            profile.vehicles = vehicles
                            prefManager.saveProfile(profile)
                            prefManager.saveVehicles(vehicles)
                        } else {
                            if (profile.vehicles != null) {
                                prefManager.saveVehicles(mutableListOf(profile.vehicles!!.first()))
                            }
                        }

                        fragmentManager!!.beginTransaction()
                            .detach(this)
                            .attach(this)
                            .commitAllowingStateLoss()
                    } else {
                        Toast.makeText(
                            activity,
                            Html.fromHtml("<font color='#FF0000' ><b>" + "Geen geldige QR-code" + "</b></font>"),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }

        }
    }

    private fun generateQr(): Bitmap {
        val bitMatrix: BitMatrix
        val json = gson.toJson(prefManager.getProfile())
        bitMatrix = MultiFormatWriter().encode(
            json, BarcodeFormat.QR_CODE,
            500, 500, null
        )

        val bitMatrixWidth = bitMatrix.width
        val bitMatrixHeight = bitMatrix.height
        val pixels = IntArray(bitMatrixWidth * bitMatrixHeight)

        val colorWhite = -0x1
        val colorBlack = -0x1000000

        for (y in 0 until bitMatrixHeight) {
            val offset = y * bitMatrixWidth
            for (x in 0 until bitMatrixWidth) {
                pixels[offset + x] = if (bitMatrix.get(x, y)) colorBlack else colorWhite
            }
        }
        val bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444)

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight)
        return bitmap
    }

}
