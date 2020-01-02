package com.me.hillfort.utils

import androidx.appcompat.app.AlertDialog
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.me.hillfort.R

fun createLoader2(activity: FragmentActivity) : AlertDialog {
    val loaderBuilder = AlertDialog.Builder(activity)
        .setCancelable(true) // 'false' if you want user to wait
        .setView(R.layout.loading)
    var loader2 = loaderBuilder.create()
    loader2.setTitle(R.string.app_name)
    loader2.setIcon(R.mipmap.ic_launcher_homer_round)

    return loader2
}

fun showLoader2(loader: AlertDialog, message: String) {
    if (!loader.isShowing()) {
        if (message != null) loader.setTitle(message)
        loader.show()
    }
}

fun hideLoader2(loader: AlertDialog) {
    if (loader.isShowing())
        loader.dismiss()
}

fun serviceUnavailableMessage2(activity: FragmentActivity) {
    Toast.makeText(
        activity,
        "Placemark Service Unavailable. Try again later",
        Toast.LENGTH_LONG
    ).show()
}

fun serviceAvailableMessage2(activity: FragmentActivity) {
    Toast.makeText(
        activity,
        "Placemark Contacted Successfully",
        Toast.LENGTH_LONG
    ).show()
}

