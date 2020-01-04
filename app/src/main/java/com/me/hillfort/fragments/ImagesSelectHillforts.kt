package com.me.hillfort.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.database.*
import com.me.hillfort.R
import com.me.hillfort.main.MainApp
import com.me.hillfort.models.HillfortModel
import com.me.hillfort.utils.*
import kotlinx.android.synthetic.main.fragment_basicreport.view.*
import kotlinx.android.synthetic.main.fragment_basicreport.view.swiperefresh
import com.me.hillfort.utils.createLoader
import com.me.hillfort.utils.hideLoader
import com.me.hillfort.utils.showLoader
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class ImagesSelectHillforts : Fragment() ,AnkoLogger, PlacemarkSelectionListener {

// this fragment generates a list of hillforts
// Select a Hillfort from the list by clicking on the hillfort
// and it will generate a list of primary images from all user sources
// using PlacemarkSelectionAdapter

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
        activity?.title = getString(R.string.activity_title_imagesselectedhilforts)
        root =  inflater.inflate(R.layout.fragment_basicreport, container, false)
        root.recyclerViewF.setLayoutManager(LinearLayoutManager(activity))

        setSwipeRefresh()


        val swipeDeleteHandler = object : SwipeToDeleteCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = root.recyclerViewF.adapter as PlacemarkSelectionAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                Toast.makeText(activity, "Deletion only at a view level", Toast.LENGTH_LONG).show()

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
            ImagesSelectHillforts().apply {
                arguments = Bundle().apply {
                }
            }
    }

    fun setSwipeRefresh() {
        info("In set swipeRefresh")
        root.swiperefresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                root.swiperefresh.isRefreshing = true
                getAllPlacemarks()
            }
        })
    }


    fun getAllPlacemarks() {
       loader = createLoader(activity!!)
        showLoader(loader, "Downloading hillforts from Firebase")
        val placemarksList = ArrayList<HillfortModel>()
// all hillfort/placemarks are stored in the hillforts
// these are then selected copies are added to each user

        app.pObj.db.child("hillforts")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    info("Firebase hillforts error : ${error.message}")
                }
                override fun onDataChange(snapshot: DataSnapshot) {
                  hideLoader(loader)
                    val children = snapshot!!.children
                    children.forEach {
                        val placemark = it.
                            getValue<HillfortModel>(HillfortModel::class.java)
                        placemarksList.add(placemark!!)
                        root.recyclerViewF.adapter =
                            PlacemarkSelectionAdapter(placemarksList, this@ImagesSelectHillforts)
                        root.recyclerViewF.adapter?.notifyDataSetChanged()
                        checkSwipeRefresh()
                        app.pObj.db.child("hillforts")
                            .removeEventListener(this)
                    }
                }
            })
    }




    fun checkSwipeRefresh() {
        if (root.swiperefresh.isRefreshing) root.swiperefresh.isRefreshing = false
    }


    // the selcted hillfort/placemark'd id is passes as a parameter to ReportImage Fragment
    //  in that fragment all primary images associated with the hillfort is displayed and can be shared
    override fun onPlacemarkClick(placemark: HillfortModel) {
        val args = Bundle()
        args.putString("selectedPlacemark",placemark.uid )
        var frag = ReportImage.newInstance()
        frag.arguments=args
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.homeFrame, frag)
            .addToBackStack(null)
            .commit()
    }




    override fun onResume() {
        super.onResume()
        info("On Resume: getAllPlacemarks")
        getAllPlacemarks()
    }



}
