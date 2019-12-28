package com.me.hillfort.models.firebase

import android.content.Context
import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.me.hillfort.helpers.readImageFromPath
import org.jetbrains.anko.AnkoLogger
import com.me.hillfort.helpers.readImageFromPath
import com.me.hillfort.models.HillfortModel

import java.io.ByteArrayOutputStream
import java.io.File

class PlacemarkFireStore(val context: Context) :  AnkoLogger {

  val placemarks = ArrayList<HillfortModel>()
  lateinit var userId: String
  lateinit var db: DatabaseReference
  lateinit var st: StorageReference

  fun findAll(): List<HillfortModel> {
    return placemarks
  }

  fun findById(id: Long): HillfortModel? {
    val foundPlacemark: HillfortModel? = placemarks.find { p -> p.id == id }
    return foundPlacemark
  }

  fun create(placemark: HillfortModel) {
   // val key = db.child("users").child(userId).child("placemarks").push().key
    val key = db.child("hillforts").push().key
    key?.let {
      placemark.uid = key
      placemarks.add(placemark)
     // db.child("users").child(userId).child("placemarks").child(key).setValue(placemark)
      db.child("hillforts").child(key).setValue(placemark)
      updateImage(placemark)
    }
  }

  fun update(placemark: HillfortModel) {
    var foundPlacemark: HillfortModel? = placemarks.find { p -> p.uid == placemark.uid }
    if (foundPlacemark != null) {
      foundPlacemark.title = placemark.title
      foundPlacemark.description = placemark.description
      foundPlacemark.image = placemark.image
      foundPlacemark.location = placemark.location
    }

//    db.child("hillforts").child(userId).child("placemarks").child(placemark.uid).setValue(placemark)
    db.child("hillforts").child(placemark.uid).setValue(placemark)
  //  db.child("hillforts").child(placemark.uid).setValue(placemark)
    if ((placemark.image.length) > 0 && (placemark.image[0] != 'h')) {
      updateImage(placemark)
    }
  }

  fun delete(placemark: HillfortModel) {
  // db.child("users").child(userId).child("placemarks").child(placemark.uid).removeValue()
    db.child("users").child(placemark.uid).removeValue()

    placemarks.remove(placemark)
  }

  fun clear() {
    placemarks.clear()
  }

  fun updateImage(placemark: HillfortModel) {
    if (placemark.image != "") {
      val fileName = File(placemark.image)
      val imageName = fileName.getName()

      var imageRef = st.child(userId + '/' + imageName)
      val baos = ByteArrayOutputStream()
      val bitmap = readImageFromPath(context, placemark.image)

      bitmap?.let {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val uploadTask = imageRef.putBytes(data)
        uploadTask.addOnFailureListener {
          println(it.message)
        }.addOnSuccessListener { taskSnapshot ->
          taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
            placemark.image = it.toString()
            db.child("hillforts").child(placemark.uid).setValue(placemark)
        //    db.child("users").child(userId).child("placemarks").child(placemark.uid).setValue(placemark)
          }
        }
      }
    }
  }

  fun fetchPlacemarks(placemarksReady: () -> Unit) {
    val valueEventListener = object : ValueEventListener {
      override fun onCancelled(dataSnapshot: DatabaseError) {
      }
      override fun onDataChange(dataSnapshot: DataSnapshot) {
        dataSnapshot!!.children.mapNotNullTo(placemarks) { it.getValue<HillfortModel>(HillfortModel::class.java) }
        placemarksReady()
      }
    }
    userId = FirebaseAuth.getInstance().currentUser!!.uid
    db = FirebaseDatabase.getInstance().reference
    st = FirebaseStorage.getInstance().reference
    placemarks.clear()
  //  db.child("users").child(userId).child("placemarks").addListenerForSingleValueEvent(valueEventListener)
    db.child("hillforts").addListenerForSingleValueEvent(valueEventListener)
  }
}