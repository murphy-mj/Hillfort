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

    override fun findOne(id: Long) : HillfortModel? {
        var foundHillfort: HillfortModel? = hillforts.find { p -> p.id == id }
        return foundHillfort
    }

    override fun findOneSetting(id: Long) : SettingsModel? {
        var foundSetting: SettingsModel? = settings.find { p -> p.id == id }
        return foundSetting
    }

    override fun findOneEmail(email: String) : SettingsModel? {
        var foundSetting: SettingsModel? = settings.find { p -> p.email == email }
        return foundSetting
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

    override fun findOneID(OneSet: SettingsModel?) : Long? {
        var foundUser: SettingsModel? = settings.find { p -> p.id == OneSet?.id }
        if (foundUser != null) {
            return foundUser?.id
        } else {
            return 0
        }
    }

    override fun findOneName(id: String) : String? {
        var id_long = id.toLong()
        var foundUser: SettingsModel? = settings.find { p -> p.id == id_long }
        return foundUser?.email
    }



    override fun update(hillfort: HillfortModel) {
   //     var foundHillfort: HillfortModel? = hillforts.find { p -> p.id == hillfort.id }

       var foundHillfort: HillfortModel? =  findOne(hillfort.id!!)
        if (foundHillfort != null) {
            foundHillfort.title = hillfort.title
            foundHillfort.description = hillfort.description
            foundHillfort.image = hillfort.image
            foundHillfort.lat = hillfort.lat
            foundHillfort.lng = hillfort.lng
            foundHillfort.zoom = hillfort.zoom
            logAll();
        }
    }

    override fun updateSetting(setting: SettingsModel) {
  //      var foundSetting: SettingsModel? = settings.find { p -> p.id == setting.id }

        var foundSetting: SettingsModel? =findOneSetting (setting.id!!)
        if (foundSetting != null) {
            foundSetting.email = setting.email
            foundSetting.password = setting.password
            logAll();
        }
    }


    override fun remove(hf: HillfortModel): Int {
        var foundHillfort: HillfortModel? = hillforts.find { p -> p.id == hf.id }
        if(foundHillfort != null){
            hillforts.remove(hf)
            return 1
        }
        return 0
    }


    internal fun logAll() {
        hillforts.forEach { info("${it}") }
    }
    internal fun logAllSettings() {
        settings.forEach { info("${it}") }
    }
}