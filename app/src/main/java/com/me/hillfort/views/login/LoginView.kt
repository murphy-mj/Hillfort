package com.me.hillfort.login

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.me.hillfort.R
//import com.bumptech.glide.Glide.init
import com.google.firebase.auth.FirebaseAuth
import com.me.hillfort.main.MainApp
//import com.me.hillfortsfinal.utils.createLoader
import com.me.hillfort.views.BaseView
//import com.me.hillfort.utils.*
import com.me.hillfort.views.login.LoginPresenter
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast

//import com.me.hillfortsfinal.utils.createLoader
//import com.me.hillfortsfinal.utils.hideLoader
//import com.me.hillfortsfinal.utils.showLoader



class LoginView : BaseView() {

  lateinit var presenter: LoginPresenter
  lateinit var app: MainApp
  lateinit var loader : AlertDialog

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_login)
    app = application as MainApp
    app.auth = FirebaseAuth.getInstance()
  //  init(toolbar, false)
   // progressBar.visibility = ProgressBar.INVISIBLE

    presenter = initPresenter(LoginPresenter(this)) as LoginPresenter

    register_user.setOnClickListener {
      val email = email_user.text.toString()
      val password = password_user.text.toString()
      if (email == "" || password == "") {
        toast("Please provide email + password")
      } else {
        presenter.doSignUp(email, password)
      }
    }

    login_user.setOnClickListener {
      val email = email_user.text.toString()
      val password = password_user.text.toString()
      if (email == "" || password == "") {
        toast("Please provide email + password")
      } else {
        presenter.doLogin(email, password)
      }
    }
   // app.auth = FirebaseAuth.getInstance()
  //  loader = createLoader(this)
  }

  override fun showProgress() {
 //   progressBar.visibility = ProgressBar.VISIBLE
  }

  override fun hideProgress() {
 //   progressBar.visibility = ProgressBar.INVISIBLE
  }

 // private fun navigateTo(fragment: Fragment) {
 //   supportFragmentManager.beginTransaction()
 //     .replace(R.id.homeFrame, fragment)
 //     .addToBackStack(null)
 //     .commit()
 // }




}