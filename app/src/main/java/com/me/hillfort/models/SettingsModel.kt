package com.me.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*


@Parcelize
data class SettingsModel(var id: Long = 0,
                         var email: String = "",
                         var password: String = ""
) : Parcelable