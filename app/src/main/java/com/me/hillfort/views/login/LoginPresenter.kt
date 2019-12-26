package com.me.hillfort.views.login

import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.storage.FirebaseStorage
import com.me.hillfort.R
import com.me.hillfort.activities.Home
import com.me.hillfort.main.MainApp
//import com.me.hillfort.models.firebase.PlacemarkFireStore
//import com.me.hillfort.utils.hideLoader
import com.me.hillfort.views.VIEW
import com.me.hillfort.views.*
import org.jetbrains.anko.toast
//import com.me.hillfort.utils.createLoader
//import com.me.hillfort.utils.hideLoader
//import com.me.hillfort.utils.showLoader
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.me.hillfort.views.BasePresenter
import com.me.hillfort.views.BaseView

class LoginPresenter(view: BaseView) : BasePresenter(view) {

 // lateinit var app: MainApp
//  var auth: FirebaseAuth = FirebaseAuth.getInstance()
 // var fireStore: PlacemarkFireStore? = null

 // init {
 //  if (app.placemarks is PlacemarkFireStore) {
 //     fireStore = app.placemarks as PlacemarkFireStore
 //   }
 // }

  fun doLogin(email: String, password: String) {

   // view?.showProgress()
    app.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(view!!) { task ->
      if (task.isSuccessful) {
      //  if (fireStore != null) {
        //  fireStore!!.fetchPlacemarks {
        //    view?.hideProgress()
        //    view?.navigateTo(VIEW.HOME)
        //  }
       // } else {
         // view?.hideProgress()
      //  Log.d(TAG, "signInWithEmail:success")
    //
        view?.toast("Sign Up OK")
   //
        val user = app.auth.currentUser
        app.database = FirebaseDatabase.getInstance().reference
        app.storage = FirebaseStorage.getInstance().reference

          view?.navigateTo(VIEW.HOME)
       // }
      } else {
     //   view?.hideProgress()
        view?.toast("Sign Up Failed: ${task.exception?.message}")
      }
    }
  }

  fun doSignUp(email: String, password: String) {
    view?.showProgress()
    app.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(view!!) { task ->
      if (task.isSuccessful) {
        view?.hideProgress()
        view?.navigateTo(VIEW.HOME)
      } else {
        view?.hideProgress()
        view?.toast("Sign Up Failed: ${task.exception?.message}")
      }
    }
  }


}