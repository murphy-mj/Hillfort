package com.me.hillfort.main


import android.app.Application
import com.me.hillfort.models.*


import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


class MainApp : Application(), AnkoLogger {

  //  val hillforts = HillfortMemStore()
    lateinit var hillforts:HillfortStore
    lateinit var settings: SettingsStore
   // val settings = HillfortMemStore()

    override fun onCreate() {
        super.onCreate()
        hillforts = HillfortJSONStore(applicationContext)
        settings = SettingsJSONStore(applicationContext)
        info("Hillfort started")
    }
}