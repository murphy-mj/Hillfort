package com.me.hillfort.fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.me.hillfort.R
import com.me.hillfort.fragments.EditFragment
import com.me.hillfort.main.MainApp
import com.me.hillfort.utils.SwipeToDeleteCallback
import com.me.hillfort.utils.SwipeToEditCallback
import com.me.hillfort.fragments.PlacemarkAdapter
import com.me.hillfort.models.HillfortModel
import com.me.hillfort.utils.*


import com.me.hillfort.fragments.PlacemarkListener
import com.me.hillfort.models.ImageModel
import kotlinx.android.synthetic.main.activity_hillfort.*
import kotlinx.android.synthetic.main.card_image.*
import kotlinx.android.synthetic.main.fragment_images.view.*
import kotlinx.android.synthetic.main.fragment_report.view.swiperefresh
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast

open class ReportImage : Fragment(), AnkoLogger,
    ImageListener {

    lateinit var app: MainApp
    lateinit var loader : AlertDialog
    lateinit var root: View
    var placemarkSelected : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
        info("User Id in On Create: ${app.auth.currentUser!!.uid}")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //this lists the primary images associated with the Placemark
        //the PlacemarkId is extracted from the one of the arguments supplied when the fragment was called

        if(arguments!!.getString("selectedPlacemark").toString() != null) {
            placemarkSelected = arguments!!.getString("selectedPlacemark").toString()
        }
        info("User Id on create view : ${placemarkSelected}")


        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_images, container, false)
        root.recyclerViewI.setLayoutManager(LinearLayoutManager(activity))
        activity?.title = getString(R.string.activity_title_ReptImage)
        setSwipeRefresh()

        val swipeDeleteHandler = object : SwipeToDeleteCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = root.recyclerViewI.adapter as ImageAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                Toast.makeText(activity, "Deletion only at a view level", Toast.LENGTH_LONG).show()

            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(root.recyclerViewI)

        val swipeEditHandler = object : SwipeToEditCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onImageClick(viewHolder.itemView.tag as ImageModel)
            }
        }
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(root.recyclerViewI)

        return root
    }

    companion object {
        @JvmStatic
      //  fun newInstance(selectedPlacemark: String?) =
        fun newInstance() =
            ReportImage().apply {
                arguments = Bundle().apply {

                }
            }
    }

    open fun setSwipeRefresh() {
        root.swiperefresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                root.swiperefresh.isRefreshing = true
                getAllImages(placemarkSelected)          }
        })
    }

    fun checkSwipeRefresh() {
        if (root.swiperefresh.isRefreshing) root.swiperefresh.isRefreshing = false
    }


// sending the selected images url to the App choosen by User

    override fun onImageClick(imageP: ImageModel) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_SUBJECT, "Share Image")
        intent.putExtra(Intent.EXTRA_TEXT, imageP!!.url.toString ())
        intent.type = "text/plain"
        startActivity(Intent.createChooser(intent, "Choose App: "))

    }



    override fun onResume() {
        super.onResume()
        if(this::class == ReportImage::class)
            getAllImages(placemarkSelected)
    }


    //should return a list of image Url's associated with the Placefort from different sources
    // each user will be given a copy of each placemark, and their selected images form part of the array returned

    fun getAllImages(placemarkId :String?) {
        loader = createLoader(activity!!)
        showLoader(loader, "Downloading Placemarks from Firebase")
        val placemarksImgList = ArrayList<ImageModel>()
        app.pObj.db.child("images").child(placemarkId!!).child("images")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    info("Firebase Placemark Image error : ${error.message}")
                }
// a new key is used to store the selected image from each user placemark
// to get at each image url, we need to loop through the outer keys
// and then access each ImageModel

                override fun onDataChange(snapshot: DataSnapshot) {
                    hideLoader(loader)
                    val children = snapshot.children
                    children.forEach {

                        val children1 = snapshot.children
                        children1.forEach {
                            val placemarkImg: ImageModel? =
                                it.getValue<ImageModel>(ImageModel::class.java)

                            placemarksImgList.add(placemarkImg!!)
                            root.recyclerViewI.adapter =
                                com.me.hillfort.fragments.ImageAdapter(
                                    placemarksImgList,
                                    this@ReportImage
                                )
                            root.recyclerViewI.adapter?.notifyDataSetChanged()
                            checkSwipeRefresh()

                            app.pObj.db.child("images").child(placemarkId).child("images")
                                .removeEventListener(this)
                        }
                    }
                }
            })
    }








// to be Implemented at a later date

    // fun deleteUserImage(userId: String, uid: String?) {
    //    app.pObj.db.child("images").child(userId).child("images")
    //        .addListenerForSingleValueEvent(
    //            object : ValueEventListener {
    //                override fun onDataChange(snapshot: DataSnapshot) {
    //                    snapshot.ref.removeValue()
    //                }
    //                override fun onCancelled(error: DatabaseError) {
    //                    info("Firebase Placemarks error : ${error.message}")
    //                }
    //            })
    // }

    //   fun deleteImage(uid: String?) {
    //       app.pObj.db.child("images").child(userId).child("images")
    //           .addListenerForSingleValueEvent(
    //               object : ValueEventListener {
    //                   override fun onDataChange(snapshot: DataSnapshot) {
    //                       snapshot.ref.removeValue()
    //                   }

//                    override fun onCancelled(error: DatabaseError) {
//                        info("Firebase images error : ${error.message}")
    //                   }
    //              })
    //  }









}
