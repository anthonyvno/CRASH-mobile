package com.example.europeesaanrijdingsformulier.profile


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.europeesaanrijdingsformulier.R
import com.example.europeesaanrijdingsformulier.utils.PrefManager
import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener
import kotlinx.android.synthetic.main.fragment_profile_vehicle_list.*


class ProfileVehicleListFragment : Fragment() {

    private lateinit var adapter: VehicleViewAdapter
    private lateinit var prefManager: PrefManager
    private lateinit var swipeTouchListener: SwipeableRecyclerViewTouchListener



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prefManager = PrefManager(activity)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_vehicle_list, container, false)
    }

    override fun onStart() {
        super.onStart()



        val vehicles = prefManager.getVehicles()
        if(vehicles!=null){
            adapter =VehicleViewAdapter(this, vehicles)
            fragment_profile_vehicle_list.adapter = adapter
        } else {
            adapter = VehicleViewAdapter(this, emptyList())
            fragment_profile_vehicle_list.adapter = adapter

        }

        swipeTouchListener = SwipeableRecyclerViewTouchListener(fragment_profile_vehicle_list,
            object : SwipeableRecyclerViewTouchListener.SwipeListener {
                override fun canSwipeLeft(position: Int): Boolean {
                    return true
                }

                override fun canSwipeRight(position: Int): Boolean {
                    return true
                }

                override fun onDismissedBySwipeLeft(recyclerView: RecyclerView, reverseSortedPositions: IntArray) {
                    for (position in reverseSortedPositions) {
                        vehicles!!.removeAt(position)
                        adapter.notifyItemRemoved(position)
                        prefManager.saveVehicles(vehicles)
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onDismissedBySwipeRight(recyclerView: RecyclerView, reverseSortedPositions: IntArray) {
                    for (position in reverseSortedPositions) {
                        vehicles!!.removeAt(position)
                        adapter.notifyItemRemoved(position)
                        prefManager.saveVehicles(vehicles)
                    }
                    adapter.notifyDataSetChanged()
                }
            })
        fragment_profile_vehicle_list.addOnItemTouchListener(swipeTouchListener)

        button_vehicle_list_add.setOnClickListener{
            this.fragmentManager!!.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right)
                .replace(R.id.container_main,ProfileVehicleDetailFragment(),"detail")
                .addToBackStack("list_to_detail")
                .commit()
        }


    }

    fun startNewFragmentForDetail(item: Vehicle) {
        val profileVehicleDetailFragment = ProfileVehicleDetailFragment()
        profileVehicleDetailFragment.addObject(item)
        this.fragmentManager!!.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right)
            .replace(R.id.container_main,profileVehicleDetailFragment,"detail")
            .addToBackStack("list_to_detail")
            .commit()
    }


}
