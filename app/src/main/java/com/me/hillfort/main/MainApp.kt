package com.me.hillfort.main


import android.app.Application
import com.me.hillfort.models.HillfortJSONStore
import com.me.hillfort.models.HillfortMemStore
import com.me.hillfort.models.HillfortStore


import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


class MainApp : Application(), AnkoLogger {

  //  val hillforts = HillfortMemStore()
    lateinit var hillforts:HillfortStore
    val settings = HillfortMemStore()

    override fun onCreate() {
        super.onCreate()
        hillforts = HillfortJSONStore(applicationContext)
        info("Hillfort started")
    }
}