package com.me.hillfort.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModel(var Uuid: String = "",
                     var firstName: String = "",
                     var lastName: String = "") : Parcelable {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "Uuid" to "Uuid",
            "firstName" to "firstName",
            "lastName"  to  "lastName"
        )
    }
}
