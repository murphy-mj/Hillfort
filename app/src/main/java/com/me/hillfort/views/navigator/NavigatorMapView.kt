package com.me.hillfort.views.navigator

import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.me.hillfort.R
import kotlinx.android.synthetic.main.activity_placemark_map.*
import kotlinx.android.synthetic.main.content_placemark_maps.*
import com.me.hillfort.helpers.readImageFromPath
import com.me.hillfort.main.MainApp
import com.me.hillfort.models.HillfortModel
import com.me.hillfort.views.BaseView
import com.me.hillfort.views.map.PlacemarkMapPresenter
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import com.me.hillfort.models.Location



class NavigatorMapView : BaseView(), GoogleMap.OnMarkerClickListener,AnkoLogger {

  lateinit var presenter: NavigatorMapPresenter
  lateinit var map : GoogleMap


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_placemark_map)
    super.init(toolbar, true)

    presenter = initPresenter (NavigatorMapPresenter(this)) as NavigatorMapPresenter

    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync {
      map = it
      map.setOnMarkerClickListener(this)
      presenter.loadPlacemarks()
    }
  }

  override fun showPlacemark(placemark: HillfortModel) {
    info("In NAV showPlacemark ??  ${placemark.title}")
    currentTitle.text = placemark.title
    currentDescription.text = placemark.description
    Glide.with(this).load(placemark.image).into(currentImage)
    presenter.doPopulateMapNav(map, placemark)
  }



  override fun showPlacemarks(placemarks: List<HillfortModel>) {
    info("NAV Showplacemarks Size : ${placemarks.size}")
    presenter.doPopulateMap(map, placemarks)
  }

  override fun onMarkerClick(marker: Marker): Boolean {
    info("In on Nav Marker Click ${marker.title}")
    presenter.doMarkerSelected(marker)
    return true
  }

  override fun onDestroy() {
    super.onDestroy()
    mapView.onDestroy()
  }

  override fun onLowMemory() {
    super.onLowMemory()
    mapView.onLowMemory()
  }

  override fun onPause() {
    super.onPause()
    mapView.onPause()
  }

  override fun onResume() {
    super.onResume()
    mapView.onResume()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    mapView.onSaveInstanceState(outState)
  }



}
