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
import com.me.hillfort.models.ImageModel
import com.me.hillfort.models.UserModel
import com.me.hillfort.views.hillfort.PlacemarkView
import org.jetbrains.anko.info

import java.io.ByteArrayOutputStream
import java.io.File

class PlacemarkFireStore(val context: Context) :  AnkoLogger {

  val placemarks = ArrayList<HillfortModel>()
  var images = ArrayList<ImageModel>()
  val users = ArrayList<UserModel>()
  lateinit var user : UserModel
  lateinit var userId: String
  lateinit var db: DatabaseReference
  lateinit var st: StorageReference

  fun findAll(): List<HillfortModel> {
    return placemarks
  }
  fun findAll2(): ArrayList<HillfortModel> {
    return placemarks
  }

  fun findAllImages(): List<ImageModel> {
    return images
  }
  fun findAllImages2(): ArrayList<ImageModel> {
    return images
  }



  fun findById(id: Long): HillfortModel? {
    val foundPlacemark: HillfortModel? = placemarks.find { p -> p.id == id }
    return foundPlacemark
  }

  fun findUserById(id: String): UserModel? {
    val foundUser: UserModel? = users.find { p -> p.Uuid == id }
    return foundUser
  }


  fun addUser() {
    userId = FirebaseAuth.getInstance().currentUser!!.uid
    db = FirebaseDatabase.getInstance().reference
    user = UserModel(Uuid = userId,firstName = "Dummy",lastName = "Name")
    db.child("users").child(userId).setValue(user)
 //   db.child("users").child(userId).setValue(userId)
   // db.child("favourites").child(userId).child(placemark.uid).setValue(placemark)
   // db.child("users").setValue(userId)
    users.add(user)
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
      // hold total number of placemarks created
      PlacemarkView.totalNumberOfHillforts = PlacemarkView.totalNumberOfHillforts + 1
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
      val imageInst = ImageModel(placemark.uid,"")
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
            imageInst.url = it.toString()
            db.child("hillforts").child(placemark.uid).setValue(placemark)
            // this is to store images associated with individual placemark in one location
            var key = db.child("images").child(placemark.uid).child("images").push().key
            key?.let {
               db.child("images").child(placemark.uid).child("images").child(key).setValue(imageInst)}
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
        dataSnapshot.children.mapNotNullTo(placemarks) { it.getValue<HillfortModel>(HillfortModel::class.java) as HillfortModel }
        placemarksReady()
      }
    }

    db = FirebaseDatabase.getInstance().reference
    st = FirebaseStorage.getInstance().reference
    placemarks.clear()
    userId = FirebaseAuth.getInstance().currentUser!!.uid

  //  db.child("users").child(userId).child("placemarks").addListenerForSingleValueEvent(valueEventListener)
    db.child("hillforts").addListenerForSingleValueEvent(valueEventListener)
  }


  fun fetchImages(uid :String, imagesReady: () -> Unit) {
    val valueEventListener = object : ValueEventListener {
      override fun onCancelled(dataSnapshot: DatabaseError) {
      }
      override fun onDataChange(dataSnapshot: DataSnapshot) {
        dataSnapshot.children.mapNotNullTo(images) { it.getValue<ImageModel>(ImageModel::class.java) }
        imagesReady()
      }
    }

    db = FirebaseDatabase.getInstance().reference
    st = FirebaseStorage.getInstance().reference
    images.clear()
    userId = FirebaseAuth.getInstance().currentUser!!.uid
    var placemarkId :String = uid

    //  db.child("users").child(userId).child("placemarks").addListenerForSingleValueEvent(valueEventListener)
    db.child("images").child(placemarkId).addListenerForSingleValueEvent(valueEventListener)
  }






  fun addFavourite(placemark: HillfortModel) {
    info("add favourite in FireStore, ${placemark.title} and ${userId}")
    // val key = db.child("users").child(userId).child("placemarks").push().key
  //  val key = db.child("favourites").child(userId).push().key
    //key?.let {
      // should only store placemark uid, but for convenience storing total hillfort/placemark
      db.child("favourites").child(userId).child(placemark.uid).setValue(placemark)
   // }
  }


 // fun addUser(user: UserModel) {
 //   db.child("users").child(userId).setValue(user)
 // }






}