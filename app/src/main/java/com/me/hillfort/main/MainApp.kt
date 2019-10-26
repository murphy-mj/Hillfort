package com.me.hillfort.main


import android.app.Application
import com.me.hillfort.models.HillfortMemStore

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


class MainApp : Application(), AnkoLogger {

    val hillforts = HillfortMemStore()
    val settings = HillfortMemStore()

    override fun onCreate() {
        super.onCreate()
        info("Hillfort started")
    }
}