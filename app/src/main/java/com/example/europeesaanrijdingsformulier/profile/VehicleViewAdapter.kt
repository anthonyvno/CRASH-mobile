package com.example.europeesaanrijdingsformulier.profile

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.europeesaanrijdingsformulier.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_vehicle.view.*

class VehicleViewAdapter(private val parentActivity: ProfileVehicleListFragment,
    private val vehicles: List<Vehicle>): RecyclerView.Adapter<VehicleViewAdapter.ViewHolder>() {


        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                // Every view has a tag that can be used to store data related to that view
                // Here each item in the RecyclerView keeps a reference to the comic it represents.
                // This allows us to reuse a single listener for all items in the list
                val item = v.tag as Vehicle

                parentActivity.startNewFragmentForDetail(item)

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_vehicle, parent, false)
            return ViewHolder(view)
        }

        //vul de viewholder
        @SuppressLint("NewApi")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val vehicle = vehicles[position]
            holder.brandmodel.text = vehicle.brand+"\n"+vehicle.model
            holder.licensePlate.text = vehicle.licensePlate
            //Picasso laadt een image via een URL string
            when (vehicle.type) {
                "Auto" ->  holder.image.setBackgroundResource(R.drawable.car_circle_icon)
                "Bus" -> holder.image.setBackgroundResource(R.drawable.bus_icon)
                "Motorfiets" -> holder.image.setBackgroundResource(R.drawable.motorcycle_icon)
                "Vrachtwagen" -> holder.image.setBackgroundResource(R.drawable.truck_icon)
                else -> {

                }
            }

            with(holder.itemView) {
                tag = vehicle // Save the vehicle represented by this view
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = vehicles.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val brandmodel: TextView = view.text_vehicle_list_brandmodel
            val licensePlate: TextView = view.text_vehicle_list_plate
            val image: ImageView = view.image_vehicle_list
        }



    }