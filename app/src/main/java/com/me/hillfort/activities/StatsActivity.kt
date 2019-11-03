package com.me.hillfort.activities

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult
import androidx.appcompat.app.AppCompatActivity
import com.me.hillfort.R
import com.me.hillfort.main.MainApp
import com.me.hillfort.models.HillfortModel
import kotlinx.android.synthetic.main.activity_stats.*
import java.util.*
import androidx.core.app.ActivityCompat.startActivityForResult
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import com.me.hillfort.activities.MapsActivity
import com.me.hillfort.helpers.readImage
import com.me.hillfort.helpers.readImageFromPath
import com.me.hillfort.helpers.showImagePicker
import com.me.models.SettingsModel
import kotlinx.android.synthetic.main.activity_hillfort.*
import kotlinx.android.synthetic.main.activity_hillfort.hillfortDescription
import kotlinx.android.synthetic.main.card_hillfort.*


class StatsActivity : AppCompatActivity() {

    var loginUser: String = ""
    lateinit var app: MainApp
    var settings: List<SettingsActivity>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)
        toolbarStats.title = title
        setSupportActionBar(toolbarStats)
        app = application as MainApp



           if (intent.hasExtra("id")) {
               toast("extra extra ID")
            var userID = intent.getStringExtra("id")
            loginUser = app.settings.findOneName(userID).toString()
          }

        //  if (intent.hasExtra("data")) {
        //   hillforts = intent.extras?.getParcelableArrayList("data")
        //  }


        textView2.setText(loginUser)
        //  textView1.setText(hillforts?.get(0)?.title!!.toString())


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_stats, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.item_stats_cancel -> {
                finish()
            }
            //  R.id.item_cancel -> {
            //      finish()
            //  }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {

        }

    }
}
