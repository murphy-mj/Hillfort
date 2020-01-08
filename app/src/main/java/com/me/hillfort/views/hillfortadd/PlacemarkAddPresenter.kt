package com.me.hillfort.views.hillfortadd

import android.annotation.SuppressLint
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.me.hillfort.helpers.*
import com.me.hillfort.views.*
//import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import com.me.hillfort.helpers.isPermissionGranted
import com.me.hillfort.helpers.showImagePicker
import com.me.hillfort.models.HillfortModel
import com.me.hillfort.models.Location
import com.me.hillfort.views.*
import org.jetbrains.anko.async

class PlacemarkAddPresenter(view: BaseView) : BasePresenter(view) {

  var map: GoogleMap? = null
  var placemark = HillfortModel()
  var defaultLocation = Location(52.245696, -122.139102, 15f)
  var edit = false;
  var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
  val locationRequest = createDefaultLocationRequest()

  init {
    if (view.intent.hasExtra("placemark_edit")) {
      edit = true
      placemark = view.intent.extras?.getParcelable<HillfortModel>("placemark_edit")!!
      view.showPlacemark(placemark)
    } else {
      if (checkLocationPermissions(view)) {
        doSetCurrentLocation()
      }
    }
  }

  @SuppressLint("MissingPermission")
  fun doSetCurrentLocation() {
    locationService.lastLocation.addOnSuccessListener {
      locationUpdate(Location(it.latitude, it.longitude))
    }
  }

  @SuppressLint("MissingPermission")
  fun doResartLocationUpdates() {
    var locationCallback = object : LocationCallback() {
      override fun onLocationResult(locationResult: LocationResult?) {
        if (locationResult != null && locationResult.locations != null) {
          val l = locationResult.locations.last()
          locationUpdate(Location(l.latitude, l.longitude))
        }
      }
    }
    if (!edit) {
      locationService.requestLocationUpdates(locationRequest, locationCallback, null)
    }
  }


  override fun doRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
    if (isPermissionGranted(requestCode, grantResults)) {
      doSetCurrentLocation()
    } else {
      locationUpdate(defaultLocation)
    }
  }


  fun doConfigureMap(m: GoogleMap) {
    map = m
    locationUpdate(placemark.location)
  }




  fun locationUpdate(location: Location) {
    placemark.location = location
    placemark.location.zoom = 10f
    map?.clear()
    val options = MarkerOptions().title(placemark.title).position(LatLng(placemark.location.lat, placemark.location.lng))
    map?.addMarker(options)
    map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(placemark.location.lat, placemark.location.lng), placemark.location.zoom))
    view?.showLocation(placemark.location)
  }




  fun doAddOrSave(title: String, description: String) {
    placemark.title = title
    placemark.description = description
    async {
      if (edit) {
       app.pObj.update(placemark)
      } else {
         app.pObj.create(placemark)
      }
      uiThread {
        view?.finish()
      }
    }
  }

  fun doAddOrSave(title: String, description: String,lat :Double,lng:Double) {
    placemark.title = title
    placemark.description = description
    var loc: Location = Location(lat,lng,8f)
    placemark.location = loc
    async {
      if (edit) {
        app.pObj.update(placemark)
      } else {
        app.pObj.create(placemark)
      }
      uiThread {
        view?.finish()
      }
    }
  }




  fun doCancel() {
    view?.finish()
  }

  fun doDelete() {
    async {
      app.pObj.delete(placemark)
      uiThread {
        view?.finish()
      }
    }
  }

  fun doSelectImage() {
    view?.let {
      showImagePicker(view!!, IMAGE_REQUEST)
    }
  }



  fun doShareImage(Image:String){
    val imageRef :String = Image.toString()
    val intent = Intent()
    intent.action = Intent.ACTION_SEND
    intent.putExtra(Intent.EXTRA_TEXT,imageRef)
    intent.type = "text/plain"
  //  startActivity(this,intent,"wd")
  //  startActivity(Intent.createChooser(this,intent,"Please select App: "))
  }

  fun doSetLocation() {
    view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST, "location", Location(placemark.location.lat, placemark.location.lng, placemark.location.zoom))
  }

  override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
    when (requestCode) {
      IMAGE_REQUEST -> {
        placemark.image = data.data.toString()
        view?.showPlacemark(placemark)
      }
      LOCATION_REQUEST -> {
        val location = data.extras?.getParcelable<Location>("location")!!
        placemark.location = location
        locationUpdate(location)
      }
    }
  }
}