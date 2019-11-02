package com.me.hillfort.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StatsModel(var no_hillforts: Double = 0.0,
                      var no_visits: Double = 0.0
) : Parcelable