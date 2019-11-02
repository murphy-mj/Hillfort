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

val JSON_FILE_V = "hillfortsVisited.json"
val gsonBuilder_V = GsonBuilder().setPrettyPrinting().create()
val listType_V = object : TypeToken<java.util.ArrayList<HillfortModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class HillfortJSONStore : HillfortStore, AnkoLogger {

    val context: Context
    var hillforts = mutableListOf<HillfortModel>()
    var hillforts_v = mutableListOf<HillfortModel>()

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
        var foundHillfort: HillfortModel? = hillfortsList.find { p -> p.id == hillfort.id }
        if (foundHillfort != null) {
            foundHillfort.title = hillfort.title
            foundHillfort.description = hillfort.description
            foundHillfort.image = hillfort.image
            foundHillfort.lat = hillfort.lat
            foundHillfort.lng = hillfort.lng
            foundHillfort.zoom = hillfort.zoom
            foundHillfort.visit_yn = hillfort.visit_yn
        }
        serialize()
    }


    override fun remove(hf: HillfortModel): Int {
        var foundHillfort: HillfortModel? = hillforts.find { p -> p.id == hf.id }
        if(foundHillfort != null){
            hillforts.remove(hf)
            serialize()
            return 1
        }
        return 0
    }





    private fun serialize() {
        val jsonString = gsonBuilder.toJson(hillforts, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        hillforts = Gson().fromJson(jsonString, listType)
    }

    fun serialize_V(file : List<HillfortModel>) {
        val jsonString = gsonBuilder_V.toJson(hillforts_v, listType_V)
        write(context, JSON_FILE_V, jsonString)
    }

    private fun deserialize_V() {
        val jsonString = read(context, JSON_FILE_V)
        hillforts_v = Gson().fromJson(jsonString, listType_V)
    }



}
