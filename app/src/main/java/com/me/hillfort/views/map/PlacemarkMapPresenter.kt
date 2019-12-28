package com.me.hillfort.views.map

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.me.hillfort.activities.HillfortAdapter
import com.me.hillfort.main.MainApp
import com.me.hillfort.models.HillfortModel
import com.me.hillfort.views.BasePresenter
import com.me.hillfort.views.BaseView
import kotlinx.android.synthetic.main.activity_hillfort_list2.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.uiThread
import java.util.*
import com.me.hillfort.models.Location

class PlacemarkMapPresenter(view: BaseView) : BasePresenter(view), AnkoLogger {




  fun doPopulateMap(map: GoogleMap, placemarks: List<HillfortModel>) {
    map.uiSettings.setZoomControlsEnabled(true)
    placemarks.forEach {
      info("In Map Presenter pop map loop  ${it.location.lat}")
      val loc  = LatLng(it.location.lat, it.location.lng)
      val options = MarkerOptions().title(it.title).position(loc)
      map.addMarker(options).tag = it
      map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.location.zoom))
    }
  }

  fun doMarkerSelected(marker: Marker) {
    //val tag = marker.tag as Long
    doAsync {
      val placemark = marker.getTag() as HillfortModel
      info("In Marker Selected  ${placemark.title}")
      uiThread {
        if (placemark != null) view?.showPlacemark(placemark)
      }
    }
  }

  fun loadPlacemarks() {
    doAsync {
      val placemarks = app.pObj.findAll()
      uiThread {
        info("load hillforts : ${placemarks.size}")
       view?.showPlacemarks(placemarks)
      }
    }
  }


}
