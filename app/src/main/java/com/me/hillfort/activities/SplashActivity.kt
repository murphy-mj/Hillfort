/*
 * Copyright (c) 2019 Razeware LLC
*/

package com.me.hillfort.activities

import android.content.Intent
//import android.support.v7.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import com.me.hillfort.R
import com.me.hillfort.views.login.LoginView

class SplashActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    makeFullScreen()
    setContentView(R.layout.activity_splash)

    // Using a handler to delay loading the MainActivity
    Handler().postDelayed({
      // Start activity
    //  startActivity(Intent(this, LoginActivity::class.java))
      startActivity(Intent(this, LoginView::class.java))
      // Animate the loading of new activity
      overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
      // Close this activity
      finish()
    }, 3000)

  }

  private fun makeFullScreen() {
    // Remove Title
    requestWindowFeature(Window.FEATURE_NO_TITLE)
    // Make Fullscreen
    window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN)
    // Hide the toolbar
    supportActionBar?.hide()
  }
}
