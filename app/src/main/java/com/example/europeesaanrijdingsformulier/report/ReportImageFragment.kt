package com.example.europeesaanrijdingsformulier.report


import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.model.Image

import com.example.europeesaanrijdingsformulier.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_report_image.*
import java.io.InputStream

class ReportImageFragment : Fragment() {

    private lateinit var report: Report
    private lateinit var images: List<Image>
    private lateinit var image: Image


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_image, container, false)
    }

    override fun onStart() {
        super.onStart()

        button_report_image_camera.setOnClickListener{
            ImagePicker.create(this) // Activity or Fragment
                .start()

        }

        button_report_image_confirm.setOnClickListener{
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
                //.addToBackStack(null)
                .commit()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            images = ImagePicker.getImages(data)
            // or get a single image only
            image = ImagePicker.getFirstImageOrNull(data)
            val gson = Gson()
            println(gson.toJson(image))
            val bm = BitmapFactory.decodeFile(image.path)
            println(bm)
           //val bitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver,image.)
            Glide.with(image_report_image_picture)
                .load(image.getPath())
                .into(image_report_image_picture);
            //image_report_image_picture.setImageBitmap(bm)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    fun addObject(item: Report) {
        this.report = item
    }

}
