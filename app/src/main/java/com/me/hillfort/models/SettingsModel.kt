package com.me.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*


@Parcelize
data class SettingsModel(var id: Long = 0,
                          var email: String = "",
                          var password: String = ""
                           ) : Parcelable


@Parcelize
data class Stats(var no_hillforts: Double = 0.0,
                 var no_visits: Double = 0.0
                 ) : Parcelable