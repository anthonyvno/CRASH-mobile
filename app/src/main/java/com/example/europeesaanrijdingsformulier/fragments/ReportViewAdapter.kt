package com.example.europeesaanrijdingsformulier.fragments

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.report.Report
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_report.view.*

class ReportViewAdapter(private val parentActivity: ReportListFragment,
                        private val reports: List<Report>): RecyclerView.Adapter<ReportViewAdapter.ViewHolder>() {


    private val onClickListener: View.OnClickListener

    init {
        onClickListener = View.OnClickListener { v ->
            // Every view has a tag that can be used to store data related to that view
            // Here each item in the RecyclerView keeps a reference to the comic it represents.
            // This allows us to reuse a single listener for all items in the list
            val item = v.tag as Report

            parentActivity.startNewFragmentForDetail(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_report, parent, false)
        return ViewHolder(view)
    }

    //vul de viewholder
    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val report = reports[position]
        val year = report.dateCrash!!.year + 1900
        holder.date.text = report.dateCrash!!.date.toString() + "/" + report.dateCrash!!.month.toString() + "/" + year.toString()
        holder.location.text = report.postalCode + " " + report.city
        if(report.profiles.first().vehicles != null){
            holder.vehicle.text = report.profiles.first().vehicles!!.first().brand + " " + report.profiles.first().vehicles!!.first().model
        }
        //Picasso laadt een image via een URL string
        Picasso.get().load("https://brandstruck.co/wp-content/uploads/2016/07/mercedes-logo-png.png").into(holder.image)

        with(holder.itemView) {
            tag = report // Save the report represented by this view
            setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount() = reports.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val date: TextView = view.text_report_list_date
        val location: TextView = view.text_report_list_location
        val vehicle: TextView = view.text_report_list_vehicle
        val image: ImageView = view.image_report_list
    }



}