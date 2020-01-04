package com.me.hillfort.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageModel(
    var name: String = "",
    var url: String = ""
) : Parcelable {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "url" to url
        )
    }
}