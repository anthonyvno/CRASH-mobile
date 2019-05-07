package com.example.europeesaanrijdingsformulier.report


import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.model.Image
import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.utils.ConnectionManager
import kotlinx.android.synthetic.main.fragment_report_image.*
import java.io.File


class ReportImageFragment : Fragment() {

    private lateinit var report: Report
    private var images = mutableListOf<Image>()
    private val connectionManager = ConnectionManager()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_report_image, container, false)
    }

    override fun onStart() {
        super.onStart()



        button_report_image_camera.setOnClickListener {
            try{
                ImagePicker.create(this)
                    .limit(4)
                    .start()
            }catch (e:Exception){
                e.printStackTrace()
                Toast.makeText(activity, "Probeer nogmaals", Toast.LENGTH_LONG
                )
            }
        }

        button_report_image_confirm.setOnClickListener {
            if(connectionManager.checkConnection(activity!!)){
                val temp = mutableListOf<String>()

                for ((counter, img) in images.withIndex()) {
                    val file = File(images[counter].path)
                    temp.add(Base64.encodeToString(file.readBytes(), Base64.DEFAULT))
                }


                report.pictures = temp.toTypedArray()


                progress_bar2.visibility = View.VISIBLE
                val fragment = ReportOverviewFragment()
                fragment.addObject(report)
                this.fragmentManager!!.beginTransaction()
                    .setCustomAnimations(
                        R.anim.enter_from_right,
                        R.anim.exit_to_left,
                        R.anim.enter_from_left,
                        R.anim.exit_to_right
                    )
                    .replace(R.id.container_main, fragment)
                    .addToBackStack(null)
                    .commit()
            } else {
                AlertDialog.Builder(activity)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Waarschuwing")
                    .setMessage("U heeft internetverbinding nodig om verder te gaan.")
                    .setNegativeButton("Ok", null)
                    .show()

            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            gridloller.background = null
            // Get a list of picked images
            images = ImagePicker.getImages(data)
            gridloller.removeAllViews()
            // or get a single image only
            if (images.size == 1) {
                gridloller.columnCount = 1
            } else {
                gridloller.columnCount = 2
            }
            gridloller.rowCount = Math.ceil((images.size.toDouble() / 2)).toInt()

            for (img in images) {

                //linearList[counter].removeAllViews()
                val iv = ImageView(activity)
                iv.scaleType = ImageView.ScaleType.FIT_XY
                iv.foreground = resources.getDrawable(R.drawable.black_border)
                //iv.adjustViewBounds = false
                Glide.with(iv)
                    .load(img.path)
                    .into(iv)

                val param = GridLayout.LayoutParams()
                if (images.size == 1) {
                    param.height = gridloller.measuredHeight
                    param.width = gridloller.measuredWidth
                } else if (images.size == 2) {
                    param.height = (gridloller.measuredHeight / 1.5).toInt()
                    param.width = gridloller.measuredWidth / 2
                } else {
                    param.height = gridloller.measuredHeight / 2
                    param.width = gridloller.measuredWidth / 2
                }


                //param.setGravity(Gravity.CENTER)

                iv.layoutParams = param
                gridloller.addView(iv)

                //println(linearList[counter].width)
                //linearList[counter].addView(iv)

            }

            //image_report_image_picture.setImageBitmap(bm)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    fun addObject(item: Report) {
        this.report = item
    }

}
