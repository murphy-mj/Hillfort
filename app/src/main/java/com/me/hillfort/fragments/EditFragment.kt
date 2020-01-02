package com.me.hillfort.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.me.hillfort.R
import com.me.hillfort.fragments.AboutUsFragment2
import com.me.hillfort.main.MainApp
import com.me.hillfort.models.HillfortModel
//import com.me.hillfortsfinal.utils.createLoader
//import com.me.hillfortsfinal.utils.hideLoader
//import com.me.hillfortsfinal.utils.showLoader
import kotlinx.android.synthetic.main.fragment_edit.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


class EditFragment : Fragment(), AnkoLogger {

    lateinit var app: MainApp
    lateinit var loader : AlertDialog
    lateinit var root: View
    var editPlacemark: HillfortModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp

        arguments?.let {
            editPlacemark = it.getParcelable("editplacemark")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_edit, container, false)
        activity?.title = getString(R.string.action_add)
     //   loader = createLoader(activity!!)

        root.textView4.setText(editPlacemark!!.title.toString())
        root.textView5.setText(editPlacemark!!.description)
      //  root.editMessage.setText(editDonation!!.message)
      //  root.editUpvotes.setText(editDonation!!.upvotes.toString())

        root.editUpdateButton.setOnClickListener {
          //  showLoader(loader, "Updating Placemark on Server...")
            updatePlacemarkData()
            updatePlacemark(editPlacemark!!.uid, editPlacemark!!)
            updateUserPlacemark(app.auth.currentUser!!.uid,
                editPlacemark!!.uid, editPlacemark!!)
        }

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance(placemark: HillfortModel) =
            EditFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("editplacemark",placemark)
                }
            }
    }

    fun updatePlacemarkData() {
        editPlacemark!!.title = root.textView4.text.toString()
        editPlacemark!!.description = root.textView5.text.toString()

     //   editDonation!!.amount = root.editAmount.text.toString().toInt()
     //   editDonation!!.message = root.editMessage.text.toString()
     //   editDonation!!.upvotes = root.editUpvotes.text.toString().toInt()
    }

    fun updateUserPlacemark(userId: String, uid: String?, placemark: HillfortModel) {
        app.database.child("user-hillforts").child(userId).child(uid!!)
        //app.database.child("users").child(userId).child("placemarks").child(placemark.uid).setValue(placemark)
        app.database.child("user-hillforts").child(userId)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.setValue(placemark)
                        activity!!.supportFragmentManager.beginTransaction()
                            .replace(R.id.homeFrame, AboutUsFragment2.newInstance())
                            .addToBackStack(null)
                            .commit()
                    //    hideLoader(loader)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Placemark error : ${error.message}")
                    }
                })
    }

    fun updatePlacemark(uid: String?, placemark: HillfortModel) {
        app.database.child("hillforts").child(placemark.uid).setValue(placemark)
    //    app.database.child("users").child(uid!!).child("placemarks").child(placemark.uid).setValue(placemark)

     //   app.database.child("users").child(app.auth.currentUser!!.uid).child("placemarks")
        app.database.child("hillforts")
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.setValue(placemark)
                    //    hideLoader(loader)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Placemark error : ${error.message}")
                    }
                })
    }
}
