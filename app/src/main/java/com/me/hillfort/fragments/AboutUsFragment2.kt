
package com.me.hillfortsfinal.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.me.hillfort.R
import com.me.hillfort.main.MainApp
import com.me.hillfort.fragments.PlacemarkAdapter
import com.me.hillfort.fragments.PlacemarkListener
import com.me.hillfort.models.HillfortModel
import kotlinx.android.synthetic.main.fragment_basicreport.view.*
//import com.me.hillfort.utils.createLoader
//import com.me.hillfort.utils.hideLoader
//import com.me.hillfort.utils.showLoader
//import kotlinx.android.synthetic.main.fragment_report.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class AboutUsFragment2 : Fragment(),AnkoLogger, PlacemarkListener {

    lateinit var root: View
    lateinit var app: MainApp
    lateinit var loader : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        activity?.title = "${app.auth.currentUser!!.uid}"
        root =  inflater.inflate(R.layout.fragment_basicreport, container, false)
        root.recyclerViewF.setLayoutManager(LinearLayoutManager(activity))
        setSwipeRefresh()




     return root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AboutUsFragment2().apply {
                arguments = Bundle().apply { }
            }
    }

    fun setSwipeRefresh() {
        root.swiperefresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                root.swiperefresh.isRefreshing = true

                getAllPlacemarks(app.auth.currentUser!!.uid)
            }
        })
    }

    fun getAllPlacemarks(userId: String?) {
        Toast.makeText(activity, "Get All placearks", Toast.LENGTH_LONG).show()
    //    loader = createLoader(activity!!)
    //    showLoader(loader, "Downloading Donations from Firebase")
        val placemarksList = ArrayList<HillfortModel>()
        app.database.child("users").child(userId!!).child("placemarks")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    info("Firebase Donation error : ${error.message}")
                }
                override fun onDataChange(snapshot: DataSnapshot) {
             //      hideLoader(loader)
                    val children = snapshot!!.children
                    children.forEach {
                        val placemark = it.
                            getValue<HillfortModel>(HillfortModel::class.java)

                        placemarksList.add(placemark!!)
                      //  root.recyclerView.adapter =
                      //      PlacemarkAdapter(placemarksList, this@AboutUsFragment2)
                      //  root.recyclerView.adapter?.notifyDataSetChanged()
                      //  checkSwipeRefresh()
                        app.database.child("users").child(userId).child("placemarks")
                            .removeEventListener(this)
                    }
                }
            })
    }










    fun checkSwipeRefresh() {
        if (root.swiperefresh.isRefreshing) root.swiperefresh.isRefreshing = false
    }

    override fun onPlacemarkClick(placemark: HillfortModel) {
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.homeFrame, EditFragment.newInstance(placemark))
            .addToBackStack(null)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        info("On Resume : ${app.auth!!.uid}")
        getAllPlacemarks(app.auth.currentUser!!.uid)
    }


  //  app.database.child("users").child(userId).child("placemarks").child(placemark.fbId).setValue(placemark)



}
