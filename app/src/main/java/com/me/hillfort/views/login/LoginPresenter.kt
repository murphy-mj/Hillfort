package com.me.hillfort.views.login

import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.storage.FirebaseStorage
//import com.me.hillfort.models.firebase.PlacemarkFireStore
//import com.me.hillfort.utils.hideLoader
import com.me.hillfort.views.VIEW
import org.jetbrains.anko.toast
//import com.me.hillfort.utils.createLoader
//import com.me.hillfort.utils.hideLoader
//import com.me.hillfort.utils.showLoader
import com.me.hillfort.models.firebase.PlacemarkFireStore
import com.me.hillfort.views.BasePresenter
import com.me.hillfort.views.BaseView

class LoginPresenter(view: BaseView) : BasePresenter(view) {

  var auth: FirebaseAuth = FirebaseAuth.getInstance()
  var fireStore: PlacemarkFireStore? = null

  init {
   if (app.pObj is PlacemarkFireStore) {
      fireStore = app.pObj as PlacemarkFireStore
    }
  }

  fun doLogin(email: String, password: String) {

   // view?.showProgress()
    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(view!!) { task ->
      if (task.isSuccessful) {
        if (fireStore != null) {
          fireStore!!.fetchPlacemarks {
            //view?.hideProgress()
            view?.navigateTo(VIEW.LIST)
          }
        } else {
          // view?.hideProgress()
          view?.navigateTo(VIEW.LIST)
        }
      } else {
        // view?.hideProgress()
        view?.toast("Sign Up Failed: ${task.exception?.message}")
      }
    }
  }

  fun doSignUp(email: String, password: String) {
    view?.showProgress()
    app.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(view!!) { task ->
      if (task.isSuccessful) {
        // view?.hideProgress()
        view?.navigateTo(VIEW.HOME)
      } else {
        // view?.hideProgress()
        view?.toast("Sign Up Failed: ${task.exception?.message}")
      }
    }
  }


}