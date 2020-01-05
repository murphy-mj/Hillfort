package com.me.hillfort.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.me.hillfort.R
import com.me.hillfort.fragments.EditFragment
import com.me.hillfort.main.MainApp
import com.me.hillfort.utils.SwipeToDeleteCallback
import com.me.hillfort.utils.SwipeToEditCallback
import com.me.hillfort.fragments.PlacemarkAdapter
import com.me.hillfort.models.HillfortModel
import com.me.hillfort.utils.*


import com.me.hillfort.fragments.PlacemarkListener
import com.me.hillfort.models.UserModel
import com.me.hillfort.views.searchhillforts.SearchHView
import kotlinx.android.synthetic.main.fragment_stats.*
import kotlinx.android.synthetic.main.fragment_stats.view.*
import kotlinx.android.synthetic.main.fragment_user.view.*
import org.jetbrains.anko.*

class StatsFragment : Fragment(), AnkoLogger, PlacemarkListener {

    lateinit var app: MainApp
    lateinit var loader : AlertDialog
    lateinit var root: View
    lateinit var db: DatabaseReference
    lateinit var User : UserModel
    var numberofAssignedHillforts : Int = 0
    var numberofHillfortsvisted : Int = 0
    lateinit var placemarksList: ArrayList<HillfortModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
        db= FirebaseDatabase.getInstance().reference
      //  arguments?.let {
      //     User = it.getParcelable("User")!!
      //  }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       // if(app.auth.currentUser != null) {
       //     User = app.pObj.findUserById(app.auth.currentUser!!.uid.toString())!!
       // }
        info("User Id on create view : ${app.auth.currentUser!!.uid}")
        placemarksList = ArrayList<HillfortModel>()
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_stats, container, false)
        activity?.title = "Stats -Current User ${app.auth.currentUser!!.uid.toString()} "
        async {
            getAllUserPlacemarks(app.auth.currentUser!!.uid)
            uiThread {
                info("in uiThread ")
                root.textView1.setText(numberofAssignedHillforts.toString())
                root.textView2.setText(numberofHillfortsvisted.toString())
            }
        }


        return root
    }
        companion object {
            @JvmStatic
            fun newInstance() =
                StatsFragment().apply {
                    arguments = Bundle().apply {


                    }
                }
        }




    override fun onResume() {
        super.onResume()
   //     if(this::class == StatsFragment::class)
     //   getAllUserPlacemarks(app.auth.currentUser!!.uid)
    }

    fun getAllUserPlacemarks(userId: String?) {
        //  loader = createLoader(activity!!)
        //  showLoader(loader, "Downloading Placemarks from Firebase")

        async {
            placemarksList.clear()
            db.child("users").child(userId!!).child("placemarks")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Users error : ${error.message}")
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        //     hideLoader(loader)
                        val children = snapshot.children
                        children.forEach {
                            val placemark: HillfortModel? =
                                it.getValue<HillfortModel>(HillfortModel::class.java)
                            info("${it.getValue<HillfortModel>(HillfortModel::class.java)?.title.toString()}")
                            info("${placemark?.title.toString()}")
                            placemarksList.add(placemark!!)
                            numberofAssignedHillforts = placemarksList.size
                            numberofHillfortsvisted = placemarksList.filter { it.visit_yn == true }.size
                            info("The number of hillforts that this user has been assigend ${placemarksList.size}")
                            //  root.recyclerViewU.adapter =
                            //    PlacemarkAdapter(
                            //      placemarksList,
                            //    this@StatsFragment
                            //  )
                            // root.recyclerViewU.adapter?.notifyDataSetChanged()

                            db.child("users").child(userId).child("placemarks")
                                .removeEventListener(this)
                        }

                    }
                })

            uiThread {
                try {
                    info("The number of hillforts that this user has been assigend ${placemarksList.size}")


                } catch (e: IndexOutOfBoundsException) {
                    info("" + { e })
                }
            }
        }
    }
    fun getAllPlacemarks(userId: String?) {
      //  loader = createLoader(activity!!)
      //  showLoader(loader, "Downloading Placemarks from Firebase")
        val placemarksList = ArrayList<HillfortModel>()
        db.child("users").child(userId!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    info("Firebase Users error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
               //     hideLoader(loader)
                    val children = snapshot.children
                    children.forEach {
                        val placemark :HillfortModel? = it.
                            getValue<HillfortModel>(HillfortModel::class.java)

                        placemarksList.add(placemark!!)
                      //  root.recyclerViewU.adapter =
                        //    PlacemarkAdapter(
                          //      placemarksList,
                            //    this@StatsFragment
                          //  )
                       // root.recyclerViewU.adapter?.notifyDataSetChanged()

                        db.child("users").child(userId)
                            .removeEventListener(this)
                    }
                }
            })
    }


    override fun onPlacemarkClick(placemark: HillfortModel) {
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.homeFrame, EditFragment.newInstance(placemark))
            .addToBackStack(null)
            .commit()
    }
}
