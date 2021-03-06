package com.me.hillfort.main


import android.app.Application
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
//import com.me.hillfort.models.HillfortStore
//import com.me.hillfort.models.SettingsStore

import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import com.me.hillfort.models.*
import com.me.hillfort.models.firebase.PlacemarkFireStore

class MainApp : Application(), AnkoLogger {

    lateinit var hillforts: HillfortStore
    lateinit var settings: SettingsStore
    lateinit var hillfortList3: MutableList<HillfortModel>
    lateinit var auth: FirebaseAuth
    lateinit var database: DatabaseReference
    lateinit var storage: StorageReference
    lateinit var userImage: Uri

    lateinit var pObj: PlacemarkFireStore

    override fun onCreate() {
        super.onCreate()
        hillforts = HillfortJSONStore(applicationContext)
        settings = SettingsJSONStore(applicationContext)
        pObj = PlacemarkFireStore(applicationContext)
        hillfortList3 = mutableListOf()
        info("Hillfort App started")
    }
}