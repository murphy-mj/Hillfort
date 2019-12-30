package com.me.hillfort.views.hillfortlist

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.me.hillfort.R
import com.me.hillfort.main.MainApp
import com.me.hillfort.views.BaseView
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import com.me.hillfort.models.HillfortModel
import com.me.hillfort.views.hillfortlist.PlacemarkAdapter
import com.me.hillfort.views.hillfortlist.PlacemarkListPresenter
import com.me.hillfort.views.hillfortlist.PlacemarkListener
import org.jetbrains.anko.info

class PlacemarkListView :  BaseView(), PlacemarkListener {

  lateinit var presenter: PlacemarkListPresenter
  lateinit var app: MainApp

  override fun onCreate(savedInstanceState: Bundle?) {
    info("In placemarks List View create")
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_hillfort_list)
    super.init(toolbar, false)
    app = application as MainApp
    presenter = initPresenter(PlacemarkListPresenter(this)) as PlacemarkListPresenter

    val layoutManager = LinearLayoutManager(this)
    recyclerView.layoutManager = layoutManager
    presenter.loadPlacemarks()

  }

  override fun showPlacemarks(placemarks: List<HillfortModel>) {
    info("In SHOW placemarks in List View ")
    info("In SHOW placemarks in List View placemarks size is  ${placemarks.size}")
    recyclerView.adapter = PlacemarkAdapter(placemarks, this)
    recyclerView.adapter?.notifyDataSetChanged()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.item_add -> presenter.doAddPlacemark()
      R.id.item_map -> presenter.doShowPlacemarksMap()
      R.id.item_navigator -> presenter.doShowPlacemarkNavMap()
      R.id.item_search -> presenter.doShowPlacemarkSearch()
      R.id.item_logout -> presenter.doLogout()
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onPlacemarkClick(placemark: HillfortModel) {
    presenter.doEditPlacemark(placemark)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    presenter.loadPlacemarks()
    super.onActivityResult(requestCode, resultCode, data)
  }



}