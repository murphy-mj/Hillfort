package com.me.hillfort.views

import android.content.Intent

import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.me.hillfort.activities.HillfortListActivity2
import com.me.hillfort.activities.Home
import com.me.hillfort.login.LoginView
import com.me.hillfort.models.HillfortModel
//import com.me.hillfort.models.PlacemarkModel
import com.me.hillfort.views.BasePresenter
//import com.me.hillfort.views.editlocation.EditLocationView
//import com.me.hillfort.views.login.LoginView
import org.jetbrains.anko.AnkoLogger
import com.me.hillfort.models.Location
//import com.me.hillfort.views.map.PlacemarkMapView
//import com.me.hillfort.views.placemark.PlacemarkView
//import com.me.hillfort.views.placemarklist.PlacemarkListView



val IMAGE_REQUEST = 1
val LOCATION_REQUEST = 2

enum class VIEW {
  LOCATION, PLACEMARK, MAPS, LIST, LOGIN, HOME, HOMEREPORT
}

open abstract class BaseView() : AppCompatActivity(), AnkoLogger {

  var basePresenter: BasePresenter? = null

  fun navigateTo(view: VIEW, code: Int = 0, key: String = "", value: Parcelable? = null) {
    // default intent
    var intent = Intent(this, HillfortListActivity2::class.java)



    when (view) {
    //  VIEW.LOCATION -> intent = Intent(this, EditLocationView::class.java)
    //  VIEW.PLACEMARK -> intent = Intent(this, PlacemarkView::class.java)
    //  VIEW.MAPS -> intent = Intent(this, PlacemarkMapView::class.java)
    //  VIEW.LIST -> intent = Intent(this, PlacemarkListView::class.java)
      VIEW.HOME -> intent = Intent(this, HillfortListActivity2::class.java)
      VIEW.LOGIN -> intent = Intent(this, LoginView::class.java)
    }
    if (key != "") {
      intent.putExtra(key, value)
    }
    startActivityForResult(intent, code)
  }

  fun initPresenter(presenter: BasePresenter): BasePresenter {
    basePresenter = presenter
    return presenter
  }

  fun init(toolbar: Toolbar, upEnabled: Boolean) {
    toolbar.title = title
    setSupportActionBar(toolbar)
    val user = FirebaseAuth.getInstance().currentUser
    if (user != null) {
      toolbar.title = "${title}: ${user.email}"
    }
    supportActionBar?.setDisplayHomeAsUpEnabled(upEnabled)
  }

  override fun onDestroy() {
    basePresenter?.onDestroy()
    super.onDestroy()
  }


  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (data != null) {
      basePresenter?.doActivityResult(requestCode, resultCode, data)
    }
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
    basePresenter?.doRequestPermissionsResult(requestCode, permissions, grantResults)
  }

  open fun showPlacemark(placemark: HillfortModel) {}
  open fun showPlacemarks(placemarks: List<HillfortModel>) {}
  open fun showLocation(location : Location) {}
  open fun showProgress() {}
  open fun hideProgress() {}
}