
package com.me.hillfort.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ReportFragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.me.hillfort.R
import com.me.hillfort.main.MainApp
import com.me.hillfort.fragments.PlacemarkAdapter
import com.me.hillfort.fragments.PlacemarkListener
import com.me.hillfort.models.HillfortModel
import com.me.hillfort.models.UserModel
import com.me.hillfort.fragments.EditFragment
import com.me.hillfort.utils.SwipeToDeleteCallback
import com.me.hillfort.utils.SwipeToEditCallback
import kotlinx.android.synthetic.main.fragment_basicreport.view.*
import kotlinx.android.synthetic.main.fragment_basicreport.view.swiperefresh
import kotlinx.android.synthetic.main.fragment_report.view.*
//import com.me.hillfort.utils.createLoader
//import com.me.hillfort.utils.hideLoader
//import com.me.hillfort.utils.showLoader
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class AdminSelectHillforts : Fragment() ,AnkoLogger, PlacemarkSelectionListener {

    lateinit var root: View
    lateinit var app: MainApp
    lateinit var loader : AlertDialog
    lateinit var userSelected : String
    lateinit var db: DatabaseReference
  //  lateinit var placemarksListFrag : ArrayList<HillfortModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
        db= FirebaseDatabase.getInstance().reference
        info("in Admin Select Hillforts on Create")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        activity?.title = "${app.auth.currentUser!!.uid}"
        root =  inflater.inflate(R.layout.fragment_basicreport, container, false)
        root.recyclerViewF.setLayoutManager(LinearLayoutManager(activity))
        userSelected = arguments!!.getString("selectedUser").toString()
        info("is there a selected User ${userSelected}")
        setSwipeRefresh()

        val swipeDeleteHandler = object : SwipeToDeleteCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = root.recyclerViewF.adapter as PlacemarkSelectionAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                addUserPlacemark(app.auth.currentUser!!.uid,
                    (viewHolder.itemView.tag as HillfortModel).uid)
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(root.recyclerViewF)

        val swipeEditHandler = object : SwipeToEditCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onPlacemarkClick(viewHolder.itemView.tag as HillfortModel)
            }
        }

        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(root.recyclerViewF)


     return root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AdminSelectHillforts().apply {
                arguments = Bundle().apply {
                   // userSelected = arguments!!.getString("selectedUser").toString()

                }
            }
    }

    fun setSwipeRefresh() {
        info("In set swipeRefresh")
        root.swiperefresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                root.swiperefresh.isRefreshing = true
              //  placemarksListFrag = app.pObj.findAll2()
                getAllPlacemarks(app.auth.currentUser!!.uid)
            }
        })
    }

    fun getAllPlacemarks(userId: String?) {
        Toast.makeText(activity, "Get All placearks", Toast.LENGTH_LONG).show()
    //    loader = createLoader(activity!!)
    //    showLoader(loader, "Downloading Donations from Firebase")
        val placemarksList = ArrayList<HillfortModel>()
       // app.database.child("users").child(userId!!).child("placemarks")
                db.child("hillforts")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    info("Firebase Hillfort error : ${error.message}")
                }
                override fun onDataChange(snapshot: DataSnapshot) {
             //      hideLoader(loader)
                    val children = snapshot!!.children
                    children.forEach {
                        val placemark = it.
                            getValue<HillfortModel>(HillfortModel::class.java)

                        placemarksList.add(placemark!!)
                        root.recyclerViewF.adapter =
                            PlacemarkSelectionAdapter(placemarksList, this@AdminSelectHillforts)
                        root.recyclerViewF.adapter?.notifyDataSetChanged()
                        checkSwipeRefresh()
                    //    app.database.child("users").child(userId).child("placemarks")
                        db.child("hillforts")
                            .removeEventListener(this)
                    }
                }
            })
    }




    fun checkSwipeRefresh() {
        if (root.swiperefresh.isRefreshing) root.swiperefresh.isRefreshing = false
    }

    override fun onPlacemarkClick(placemark: HillfortModel) {
        db.child("users").child(userSelected).child("placemarks").child(placemark.uid).setValue(placemark)
       // only delete from favourites
       // activity!!.supportFragmentManager.beginTransaction()
       //     .replace(R.id.homeFrame, EditFragment.newInstance(placemark))
       //     .addToBackStack(null)
       //     .commit()
    }

    override fun onResume() {
        super.onResume()
        info("On Resume : ${app.auth!!.uid}")
        getAllPlacemarks(app.auth.currentUser!!.uid)
    }


  //  app.database.child("users").child(userId).child("placemarks").child(placemark.fbId).setValue(placemark)

    fun addUserPlacemark(userId: String, uid: String?) {
      //  db.child("user").child(userSelected).child(uid!!)
        db.child("user")
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        db.child("users").setValue(userSelected)
                        db.child("users").child(userSelected).setValue(uid)
                      //  snapshot.ref.removeValue()
                    }
                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Placemarks error : ${error.message}")
                    }
                })
    }

    fun deleteFavPlacemark(uid: String?) {
        app.database.child("favourites").child(app.auth.currentUser!!.uid).child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.removeValue()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Favourite error : ${error.message}")
                    }
                })
    }

}
