package com.me.hillfort.models

import android.content.Context
import android.graphics.ColorSpace
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import java.nio.file.Files.exists
import com.me.hillfort.helpers.*
import java.util.*

val JSON_FILE = "hillforts.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<java.util.ArrayList<HillfortModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class HillfortJSONStore : HillfortStore, AnkoLogger {

    val context: Context
    var hillforts = mutableListOf<HillfortModel>()

    constructor (context: Context) {
        this.context = context
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<HillfortModel> {
        return hillforts
    }

    override fun findOne(id: Long) : HillfortModel? {
        var foundHillfort: HillfortModel? = hillforts.find { p -> p.id == id }
        return foundHillfort
    }



    override fun create(hillfort: HillfortModel) {
        hillfort.id = generateRandomId()
        hillforts.add(hillfort)
        serialize()
    }

    override fun update(hillfort: HillfortModel) {
        val hillfortsList = findAll() as ArrayList<HillfortModel>
        var foundPlacemark: HillfortModel? = hillfortsList.find { p -> p.id == hillfort.id }
        if (foundPlacemark != null) {
            foundPlacemark.title = hillfort.title
            foundPlacemark.description = hillfort.description
            foundPlacemark.image = hillfort.image
            foundPlacemark.lat = hillfort.lat
            foundPlacemark.lng = hillfort.lng
            foundPlacemark.zoom = hillfort.zoom
        }
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(hillforts, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        hillforts = Gson().fromJson(jsonString, listType)
    }
}
