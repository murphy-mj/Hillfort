package com.me.hillfort.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.android.parcel.Parcelize
import com.google.firebase.database.IgnoreExtraProperties


@Parcelize
data class HillfortModel(var uid: String = "",
                          var id: Long = 0,
                          var title: String = "",
                          var description: String = "",
                          var image: String = "",
                          var profilepic: String = "",
                          var visit_date:String = "na",
                          var visit_yn: Boolean = false,
                          var location : Location = Location()
                         ) : Parcelable {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "id" to id,
            "title" to title,
            "description" to description,
            "image" to image,
            "profilepic" to profilepic,
            "visit_date" to visit_date,
            "visit_yn" to visit_yn,
             "location" to location
        )
    }
}




@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable