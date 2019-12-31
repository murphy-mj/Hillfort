package com.me.hillfort.views.hillfortlist

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.me.hillfort.main.MainApp
import com.me.hillfort.views.BasePresenter
import com.me.hillfort.views.BaseView
import com.me.hillfort.views.VIEW
//import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import com.me.hillfort.models.HillfortModel
import com.me.hillfort.views.searchhillforts.SearchHView
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.async
import org.jetbrains.anko.info
import java.util.ArrayList


class PlacemarkListPresenter(view: BaseView) : BasePresenter(view),AnkoLogger {




  fun doAddPlacemark() {
    view?.navigateTo(VIEW.PLACEMARK)
  }

  fun doEditPlacemark(placemark: HillfortModel) {
    view?.navigateTo(VIEW.PLACEMARK, 0, "placemark_edit", placemark)
  }

  fun doShowPlacemarksMap() {
    view?.navigateTo(VIEW.MAPS)
  }

  fun doShowPlacemarkNavMap() {
    view?.navigateTo(VIEW.NAVIGATOR)
  }


 // fun loadPlacemarks() {
 //   doAsync {
 //   //  val placemarks = app.placemarks.findAll()
 //     val placemarks:List<HillfortModel> = getAllUsersHillforts()
 //     uiThread {
 //       view?.showPlacemarks(placemarks)
 //     }
 //   }
 // }




  fun loadPlacemarks() {
    async {
      val placemarks = app.pObj.findAll()
      SearchHView.imageModelArrayList = app.pObj.findAll2()
      uiThread {
        view?.showPlacemarks(placemarks)
      }
    }
  }
  fun doLogout() {
    //app.placemarks.clear()

    FirebaseAuth.getInstance().signOut()
    view?.navigateTo(VIEW.LOGIN)
  }

  fun doShowPlacemarkSearch() {
    info("In List Presenter, heading for View SEARCH")
    view?.navigateTo(VIEW.SEARCH)

  }



 // fun getAllUsersHillforts(): List<HillfortModel> {


  fun loadPlacemarks2(app : MainApp) {
    info("In Load placemarks in List Presenter ")
    async {
      //  var hillfortList = ArrayList<HillfortModel>()
      val hillfortList: MutableList<HillfortModel> = mutableListOf()
      val hillfortList2: MutableList<HillfortModel> = mutableListOf()
      app.database.child("hillforts")
        .addValueEventListener(object : ValueEventListener {
          override fun onCancelled(error: DatabaseError) {
            // bla
          }

          override fun onDataChange(snapshot: DataSnapshot) {
            val children = snapshot.children
            snapshot.children.mapNotNullTo(hillfortList) {
              it.getValue<HillfortModel>(HillfortModel::class.java)
            }
            info("In Load placemarks in List Presenter hillfortList size ${hillfortList.size}")

            children.forEach {
              val hillfort = it.getValue<HillfortModel>(HillfortModel::class.java)
              info("adding hillfort to list : ${hillfort!!.description}")
              hillfortList2.add(hillfort!!)
            }
            info("In Load placemarks in List Presenter hillfortList2 size ${hillfortList2.size}")
            app.database.child("hillforts").removeEventListener(this)
          }
        })
      uiThread {
        info("In Load placemarks in List Presenter UITread hillfortList2 size ${hillfortList2.size}")
        view?.showPlacemarks(hillfortList2)
      }
    }
  }



  }
