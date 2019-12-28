package com.me.hillfort.views.editlocation

import android.content.Intent
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.me.hillfort.models.Location
import com.me.hillfort.views.BasePresenter
import com.me.hillfort.views.BaseView
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class EditLocationPresenter(view: BaseView) : BasePresenter(view),AnkoLogger {

  var location = Location()

  init {
    location = view.intent.extras?.getParcelable<Location>("location")!!
  }

  fun doConfigureMap(map: GoogleMap) {
    info("In Edit Presenter ${location.lat}")
    val loc = LatLng(location.lat, location.lng)
    val options = MarkerOptions()
      .title("Hillfort")
      .snippet("GPS : " + loc.toString())
      .draggable(true)
      .position(loc)
    map.addMarker(options)
    map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, location.zoom))
  }

  fun doUpdateLocation(lat: Double, lng: Double) {
    location.lat = lat
    location.lng = lng
    doSave()
  }

  fun doSave() {
    val resultIntent = Intent()
    resultIntent.putExtra("location", location)
    view?.setResult(0, resultIntent)
    view?.finish()
  }

  fun doUpdateMarker(marker: Marker) {
    val loc = LatLng(location.lat, location.lng)
    marker.setSnippet("GPS : " + loc.toString())
  }
}