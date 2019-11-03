package com.me.hillfort.models

import android.content.Context
import android.graphics.ColorSpace
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import java.nio.file.Files.exists
import com.me.hillfort.helpers.*
import com.me.models.SettingsModel
import java.util.*


val JSON_FILE2 = "settings.json"
val gsonBuilder2 = GsonBuilder().setPrettyPrinting().create()
val listType2 = object : TypeToken<java.util.ArrayList<SettingsModel>>() {}.type

fun generateRandomId2(): Long {
    return Random().nextLong()
}

class SettingsJSONStore : SettingsStore, AnkoLogger {

    val context: Context
    var settings = mutableListOf<SettingsModel>()

    constructor (context: Context) {
        this.context = context
        if (exists(context, JSON_FILE2)) {
            deserialize()
        }
    }

    override fun findAllSettings(): MutableList<SettingsModel> {
        return settings
    }

    override fun findOneSetting(id: Long) : SettingsModel? {
        var foundUser: SettingsModel? = settings.find { p -> p.id == id }
        return foundUser
    }
    override fun findOneEmail(email: String) : SettingsModel? {
        var foundUser: SettingsModel? = settings.find { p -> p.email == email }
        return foundUser
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
        if (foundUser != null) {
            return foundUser?.email
        }
        return "no User"
    }


    override fun createSetting(user: SettingsModel) {
        user.id = generateRandomId2()
        settings.add(user)
        serialize()
    }

    override fun updateSetting(user: SettingsModel) {
        val settingsList = findAllSettings() as ArrayList<SettingsModel>
        var foundUser: SettingsModel? = settingsList.find { p -> p.id == user.id }
        if (foundUser != null) {
            foundUser.email = user.email
            foundUser.password = user.password
        }
        serialize()
    }




    private fun serialize() {
        val jsonString = gsonBuilder2.toJson(settings, listType2)
        write(context, JSON_FILE2, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE2)
        settings = Gson().fromJson(jsonString, listType2)
    }
}
