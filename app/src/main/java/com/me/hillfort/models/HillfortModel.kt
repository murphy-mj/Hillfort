package com.me.hillfort.models

import com.google.firebase.database.Exclude
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*


@Parcelize
data class HillfortModel(var uid: String? = "",
                         var id: Long = 0,
                          var title: String = "",
                          var description: String = "",
                          var image: String = "",
                          var lat : Double = 0.0,
                          var lng: Double = 0.0,
                          var zoom: Float = 15f,
                          var visit_date:String = "na",
                          var visit_yn: Boolean = false ) : Parcelable {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "id" to id,
            "title" to title,
            "description" to description,
            "image" to image,
            "lat" to lat,
            "lng" to lng,
            "zoom" to zoom,
            "visit_date" to visit_date,
            "visit_yn" to visit_yn
        )
    }
}




@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable