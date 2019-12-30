package com.me.hillfort.views.searchhillforts

import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.me.hillfort.main.MainApp
import com.me.hillfort.views.BasePresenter
import com.me.hillfort.views.BaseView
import com.me.hillfort.views.VIEW
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.async
import org.jetbrains.anko.info
import org.jetbrains.anko.uiThread

class SearchHPresenter(view: BaseView) : BasePresenter(view), AnkoLogger {

    fun loadHillfortsSearch(containingString: String) {
      //  view?.showPlacemarks(app.hillforts.findAll().filter { it.title.toLowerCase().contains(containingString.toLowerCase()) })
    }


    fun loadPlacemarks() {
       // Toast.makeText(this,"in Load placemarks in Search Presenter",Toast.LENGTH_LONG).show()

        info("in loadPlacemarks in Search Presenter")
        async {
            SearchHView.imageModelArrayList = app.pObj.findAll2()
            uiThread {
                view?.showPlacemarks(SearchHView.imageModelArrayList)
            }
        }
    }


    fun doShowPlacemarkSearch() {
        info("In Search Presenter, heading for View SEARCH")
        view?.navigateTo(VIEW.SEARCH)

    }

    fun doLogout(){
        FirebaseAuth.getInstance().signOut()
        view?.navigateTo(VIEW.LOGIN)
    }

}