package com.me.hillfort.views.hillfort

import com.me.hillfort.R
import com.me.hillfort.views.BaseView
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast
import com.me.hillfort.helpers.readImageFromPath
import com.me.hillfort.models.Location
import com.me.hillfort.models.HillfortModel
//import kotlinx.android.synthetic.main.activity_edit_location.*


class PlacemarkView : BaseView(), AnkoLogger {

  lateinit var presenter: PlacemarkPresenter
  var placemark = HillfortModel()
  var edit = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_hillfort)
    super.init(toolbarAdd, true)

    presenter = initPresenter (PlacemarkPresenter(this)) as PlacemarkPresenter



    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync {
      presenter.doConfigureMap(it)
      it.setOnMapClickListener { presenter.doSetLocation() }
    }

    chooseImage.setOnClickListener { presenter.doSelectImage() }

    button_share.setOnClickListener {
      val intent = Intent()
      intent.action = Intent.ACTION_SEND
      intent.putExtra(Intent.EXTRA_SUBJECT,"Share Image")
      intent.putExtra(Intent.EXTRA_TEXT, placemark!!.image!!.toString())
      toast("share what ${ placemark!!.image!!.toString()}")
      intent.type = "text/plain"
      startActivity(Intent.createChooser(intent,"Choose App: "))
    }
   //   presenter.doShareImage(placemark!!.image)
    if (intent.hasExtra("hillfort_edit")) {
      edit = true
      placemark = intent.extras?.getParcelable<HillfortModel>("hillfort_edit")!!
      chooseImage.setText(R.string.button_selectImage)
    } else {
      chooseImage.setText(R.string.button_selectImage)
    }

  }




  override fun showPlacemark(placemark: HillfortModel) {
    hillfortTitle.setText(placemark.title)
    hillfortDescription.setText(placemark.description)
    Glide.with(this).load(placemark.image).into(hillfortImage);
    if (placemark.image != null) {
      chooseImage.setText(R.string.change_placemark_image)
    }
    this.showLocation(placemark.location)
  }

  override fun showLocation(location: Location) {
    location_lat.setText("%.6f".format(location.lat))
    location_lng.setText("%.6f".format(location.lng))
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_hillfort, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.item_delete -> {
        presenter.doDelete()
      }

      R.id.item_location -> {
        presenter.doSetLocation()
      }

      R.id.item_save -> {
        if (hillfortTitle.text.toString().isEmpty()) {
          toast(R.string.enter_placemark_title)
        } else {
          presenter.doAddOrSave(hillfortTitle.text.toString(), hillfortDescription.text.toString())
        }
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (data != null) {
      presenter.doActivityResult(requestCode, resultCode, data)
    }
  }

  override fun onBackPressed() {
    presenter.doCancel()
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
    presenter.doResartLocationUpdates()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    mapView.onSaveInstanceState(outState)
  }

  companion object{
//
    var totalNumberOfHillforts : Int = 0
  }


}


