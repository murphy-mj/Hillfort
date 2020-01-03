package com.me.hillfort.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ReportFragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.me.hillfort.R
import com.me.hillfort.fragments.PlacemarkAdapter

import com.me.hillfort.fragments.PlacemarkListener
import com.me.hillfort.main.MainApp
import com.me.hillfort.models.HillfortModel
import com.me.hillfort.utils.createLoader
import com.me.hillfort.utils.hideLoader
import com.me.hillfort.utils.showLoader
import com.me.hillfort.utils.*
import com.me.hillfort.fragments.EditFragment
import kotlinx.android.synthetic.main.fragment_report.view.*
//import kotlinx.android.synthetic.main.fragment_report.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class ReportAllFragment : ReportFragment(),
    PlacemarkListener , AnkoLogger{

    lateinit var root: View
    lateinit var app: MainApp
    lateinit var loader : AlertDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_report, container, false)
        activity?.title = getString(R.string.menu_report_all)

        root.recyclerView.setLayoutManager(LinearLayoutManager(activity))
        setSwipeRefresh()

        return root
    }
// not required as import from ReportFragment
    //  @JvmStatic allowing java files to use companion object as Static

    companion object {
        @JvmStatic
        fun newInstance() =
            ReportAllFragment().apply {
                arguments = Bundle().apply { }
            }
    }

    fun setSwipeRefresh() {
        root.swiperefresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                root.swiperefresh.isRefreshing = true
                getAllUsersPlacemarks()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        getAllUsersPlacemarks()
    }

    fun getAllUsersPlacemarks() {
      //  loader = createLoader(activity!!)
        showLoader(loader, "Downloading All Users Donations from Firebase")
        val placemarksList = ArrayList<HillfortModel>()
        app.pObj.db.child("hillforts")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    info("Firebase Donation error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    hideLoader(loader)
                    val children = snapshot.children
                    children.forEach {
                        val placemark = it.
                            getValue<HillfortModel>(HillfortModel::class.java)

                        placemarksList.add(placemark!!)
                        root.recyclerView.adapter =
                            PlacemarkAdapter(placemarksList, this@ReportAllFragment)
                        root.recyclerView.adapter?.notifyDataSetChanged()
                        checkSwipeRefresh()

                        app.pObj.db.child("hillforts").removeEventListener(this)
                    }
                }
            })
    }


    fun checkSwipeRefresh() {
        if (root.swiperefresh.isRefreshing) root.swiperefresh.isRefreshing = false
    }

    fun deleteUserPlacemark(userId: String, uid: String?) {
        app.pObj.db.child("user-hillforts").child(userId).child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.removeValue()
                    }
                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Placemarks error : ${error.message}")
                    }
                })
    }


    fun deletePlacemark(uid: String?) {
        app.pObj.db.child("placemarks").child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.removeValue()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Placemark error : ${error.message}")
                    }
                })
    }

    override fun onPlacemarkClick(placemark: HillfortModel) {
     //  activity!!.supportFragmentManager.beginTransaction()
     //       .replace(R.id.homeFrame, EditFragment.newInstance(placemark))
     //       .addToBackStack(null)
     //       .commit()
    }


}
