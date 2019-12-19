package com.me.hillfort.views

import android.content.Intent
import com.me.hillfort.main.MainApp
import com.me.hillfort.views.BaseView

open class BasePresenter(var view: BaseView?) {

  var app: MainApp =  view?.application as MainApp

  open fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
  }

  open fun doRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
  }

  open fun onDestroy() {
    view = null
  }
}