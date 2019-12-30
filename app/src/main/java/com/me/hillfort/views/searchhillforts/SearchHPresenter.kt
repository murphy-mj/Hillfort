package com.me.hillfort.views.searchhillforts

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.me.hillfort.main.MainApp
import com.me.hillfort.views.BasePresenter
import com.me.hillfort.views.BaseView
import com.me.hillfort.views.VIEW
import org.jetbrains.anko.async
import org.jetbrains.anko.uiThread


class SearchHPresenter(view: BaseView) : BasePresenter(view) {

    fun loadHillfortsSearch(containingString: String) {
        view?.showPlacemarks(app.hillforts.findAll().filter { it.title.toLowerCase().contains(containingString.toLowerCase()) })
    }


    fun loadPlacemarks() {
        async {
            val placemarks = app.pObj.findAll()
            uiThread {
                view?.showPlacemarks(placemarks)
            }
        }
    }


    fun doShowPlacemarkSearch() {
        view?.navigateTo(VIEW.SEARCH)

    }

    fun doLogout(){
        FirebaseAuth.getInstance().signOut()
        view?.navigateTo(VIEW.LOGIN)
    }

}