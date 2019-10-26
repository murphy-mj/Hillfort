package com.me.hillfort.models


import com.me.models.SettingsModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

var lastSetId = 0L

internal fun getSetId(): Long {
    return lastSetId++
}


class HillfortMemStore : HillfortStore, SettingsStore, AnkoLogger {

    val hillforts = ArrayList<HillfortModel>()
    val settings = ArrayList<SettingsModel>()

    override fun findAll(): List<HillfortModel> {
        return hillforts
    }

    override fun findAllSettings(): List<SettingsModel> {
        return settings
    }

    override fun create(hillfort: HillfortModel) {
        hillfort.id = getId()
        hillforts.add(hillfort)
        logAll()
    }

    override fun createSetting(setting: SettingsModel) {
        setting.id = getSetId()
        settings.add(setting)
        logAll()
    }


    override fun update(hillfort: HillfortModel) {
        var foundHillfort: HillfortModel? = hillforts.find { p -> p.id == hillfort.id }
        if (foundHillfort != null) {
            foundHillfort.title = hillfort.title
            foundHillfort.description = hillfort.description
            foundHillfort.image = hillfort.image
            logAll();
        }
    }

    override fun updateSetting(setting: SettingsModel) {
        var foundSetting: SettingsModel? = settings.find { p -> p.id == setting.id }
        if (foundSetting != null) {
            foundSetting.email = setting.email
            foundSetting.password = setting.password
            logAll();
        }
    }





    internal fun logAll() {
        hillforts.forEach { info("${it}") }
    }
}