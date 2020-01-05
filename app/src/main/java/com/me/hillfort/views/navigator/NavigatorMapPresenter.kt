package com.me.hillfort.views.navigator

import android.annotation.SuppressLint
import android.graphics.Color
import com.beust.klaxon.*
import com.bumptech.glide.Glide.init
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.OnMapReadyCallback
import com.me.hillfort.activities.HillfortAdapter
import com.me.hillfort.helpers.checkLocationPermissions
import com.me.hillfort.helpers.createDefaultLocationRequest
import com.me.hillfort.helpers.isPermissionGranted

import com.me.hillfort.models.HillfortModel
import com.me.hillfort.views.BasePresenter
import com.me.hillfort.views.BaseView
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

import java.util.*
import com.me.hillfort.models.Location
//import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URL
import org.jetbrains.anko.uiThread as uiThread1
import org.jetbrains.anko.async
import java.io.IOException

class NavigatorMapPresenter(view: BaseView) : BasePresenter(view), AnkoLogger {

    val LatLongB = LatLngBounds.Builder()

    var defaultLocation = Location(52.245696, -122.139102, 5f)
    var currentLatLng: LatLng = LatLng(38.00, -122.00)

    var locationService: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(view)

    lateinit var hillfort: HillfortModel
    lateinit var mapPre: GoogleMap


    // this function adds all placemarks to the map

    fun doPopulateMap(map: GoogleMap, placemarks: List<HillfortModel>) {
        map.uiSettings.setZoomControlsEnabled(true)
        mapPre = map
        placemarks.forEach {
            info("In NAV Map Presenter pop map loop  ${it.location.lat}")
            val loc = LatLng(it.location.lat, it.location.lng)
            val options = MarkerOptions().title(it.title).position(loc)
            map.addMarker(options).tag = it
           // map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.location.zoom))
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 6f))
        }
    }

    // once you select a placemark
    // this functions clears map of all markers, just add the selected marker and the current location
    // then draws a path to and from both locations

    fun doPopulateMapNav(map: GoogleMap, placemark: HillfortModel) {
        map.clear()
        map.uiSettings.setZoomControlsEnabled(true)
        info("In Map NAV Presenter Individual Placemark lat  ${placemark.location.lat}")
        val loc = LatLng(placemark.location.lat, placemark.location.lng)
        val options = MarkerOptions().title(placemark.title).position(loc)
        map.addMarker(options).tag = placemark

        // generating curent location on the map

        async {
            locationService.lastLocation!!.addOnSuccessListener {
                currentLatLng = LatLng(it.latitude, it.longitude)

            }
            uiThread1 {

                val optionsCurrent =
                    MarkerOptions().title("CurrentLocation").position(currentLatLng)
                map.addMarker(optionsCurrent)
            }
        }



        info("In Map NAV Presenter Individual Current Lat  ${placemark.location.lat}")
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, placemark.location.zoom))
        // Declare polyline object and set up color and width
        val optionsPath = PolylineOptions()
        optionsPath.color(Color.RED)
        optionsPath.width(5f)

        val url = getURL(loc, currentLatLng)

        async {
            // Connect to URL, download content and convert into string asynchronously
            val result = URL(url).readText()
            info("get Url" + result)

            uiThread1 {
                // When API call is done, create parser and convert into JsonObjec
                val parser: Parser = Parser()
                val stringBuilder: StringBuilder = StringBuilder(result)
                val json: JsonObject = parser.parse(stringBuilder) as JsonObject
                // get to the correct element in JsonObject
                val routes = json.array<JsonObject>("routes")

                try {
                    val points = routes!!["legs"]["steps"][0] as JsonArray<JsonObject>
                    val polypts =
                        points.flatMap { decodePoly(it.obj("polyline")?.string("points")!!) }
                    optionsPath.add(loc)
                    LatLongB.include(loc)


                    for (point in polypts) {
                        optionsPath.add(point)
                        LatLongB.include(point)
                    }


                } catch (e: IndexOutOfBoundsException) {
                    info("" + { e })
                }
                // For every element in the JsonArray, decode the polyline string and pass all points to a List
                //   val polypts = points.map { it.obj("polyline")?.string("points")!!  }

                // Add  points to polyline and bounds

                optionsPath.add(currentLatLng)
                LatLongB.include(currentLatLng)

                // build bounds
                val bounds = LatLongB.build()
                // add polyline to the map
                map.addPolyline(optionsPath)
                // show map with route centered
                map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
            }
        }

// the function will only work when you have a Google Account, hence i have added a try and catch block around the routes request
// as it gives an access error

    }


    private fun getURL(from: LatLng, to: LatLng): String {
        val origin = "origin=" + from.latitude + "," + from.longitude
        val dest = "destination=" + to.latitude + "," + to.longitude
        val sensor = "sensor=false"
        val params = "$origin&$dest&$sensor"
        return "https://maps.googleapis.com/maps/api/directions/json?$params"
        // &Key=api key not applied, as no account set up
    }


    fun doMarkerSelected(marker: Marker) {
        hillfort = marker.getTag() as HillfortModel
        info("In Nav Marker Selected  ${hillfort.title}")
        //   if (hillfort != null) view?.showPlacemark(hillfort)
        view?.showPlacemark(hillfort)

    }


    fun loadPlacemarks() {
        async {
            val placemarks = app.pObj.findAll()
            uiThread1 {
                info("load hillforts from Nav : ${placemarks.size}")
                view?.showPlacemarks(placemarks)
            }
        }
    }


    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(Location(it.latitude, it.longitude))
        }
    }


    override fun doRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (isPermissionGranted(requestCode, grantResults)) {
            doSetCurrentLocation()
        } else {
            locationUpdate(defaultLocation)
        }
    }


    fun locationUpdate(location: Location) {
        if (hillfort == null) {
            info("location Update Nav Map, hillfort is null")
        } else {
            hillfort.location = location
            hillfort.location.zoom = 15f
            mapPre.clear()
            val options = MarkerOptions().title(hillfort.title)
                .position(LatLng(hillfort.location.lat, hillfort.location.lng))
            mapPre.addMarker(options)
            mapPre.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        hillfort.location.lat,
                        hillfort.location.lng
                    ), hillfort.location.zoom
                )
            )
            view?.showLocation(hillfort.location)
        }
    }

    /**
     * Method to decode polyline points
     * Courtesy : https://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     */


    private fun decodePoly(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(
                lat.toDouble() / 1E5,
                lng.toDouble() / 1E5
            )
            poly.add(p)
        }

        return poly
    }



    //addition functions not used

    //    private lateinit var lastLocation: Location
//    val locationRequest = createDefaultLocationRequest()


//    @SuppressLint("MissingPermission")
//    fun doSetCurrentLocationNav() {
//        locationService.lastLocation.addOnSuccessListener {
//            currentLatLng = LatLng(it.latitude, it.longitude)
//        }
//    }

//    @SuppressLint("MissingPermission")
//    fun doResartLocationUpdates() {
//        var locationCallback = object : LocationCallback() {
//            override fun onLocationResult(locationResult: LocationResult?) {
//                if (locationResult != null && locationResult.locations != null) {
//                    val l = locationResult.locations.last()
//                    locationUpdate(Location(l.latitude, l.longitude))
//                }
//            }
//        }
        //    if (!edit) {
        //        locationService.requestLocationUpdates(locationRequest, locationCallback, null)
        //    }
  //  }



    //  init {
    //       if (checkLocationPermissions(view)) {
    //           doSetCurrentLocation()
    //       }
    //   }

}
