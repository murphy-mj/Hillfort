package com.me.hillfort.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.*
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import com.me.hillfort.activities.MapsActivity
import com.me.hillfort.R
import com.me.hillfort.helpers.readImage
import com.me.hillfort.helpers.readImageFromPath
import com.me.hillfort.helpers.showImagePicker
import com.me.hillfort.main.MainApp
import com.me.hillfort.models.Location
import com.me.hillfort.models.HillfortModel
import com.me.models.SettingsModel
import kotlinx.android.synthetic.main.activity_hillfort.*
import kotlinx.android.synthetic.main.activity_hillfort.hillfortDescription
import kotlinx.android.synthetic.main.activity_hillfort_list2.*
import kotlinx.android.synthetic.main.activity_stats.*
import kotlinx.android.synthetic.main.card_hillfort.*
import java.util.*


class Settings2Activity : AppCompatActivity(), AnkoLogger, HillfortListener {

    var hillforts = mutableListOf<HillfortModel>()

    var hillfortsNotVisited = hillforts.toMutableList()

    lateinit var app: MainApp
    var userEmail: String = ""
    var numberOfHillforts:Int = 0
    var numberVisited : Int = 0
    var numberWithoutDescriptions:Int = 0
//    var lastOneVisted? : Date = 1-1-2008


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)
        toolbarStats.title = title
        setSupportActionBar(toolbarStats)
        info("STATS Activity started..")

        app = application as MainApp


    //    btnSetAdd.setOnClickListener() {
    //        setting.email = settingEmail.text.toString()
    //        setting.password = settingPassword.text.toString()
    //        if (setting.email.isEmpty()|| setting.password.isEmpty())  {
    //            toast(R.string.hint_settingsAll)
    ///        } else {
    //                app.settings.createSetting(setting.copy())
    //            }
    //        info("add Button Pressed")
    //        setResult(AppCompatActivity.RESULT_OK)
    //        finish()
    //    }

        if (intent.hasExtra("email")) {
            toast("extra extra Email")
            userEmail = intent.getStringExtra("email")
          // loginUser = app.settings.findOneEmail(userID).toString()
        }

        hillforts = app.hillforts.findAll().toMutableList()

        if(hillforts != null) {
            numberOfHillforts = hillforts.size
            numberVisited  = hillforts.filter {it.visit_yn}.size
          //  hillfortsNotVisited = hillforts.filter {it.visit_yn}.toMutableList()

        }

        textView2.text = userEmail
        textView1.text = "This is the story so far. I ${userEmail} , have ${numberOfHillforts} in my collection. I have visited ${numberVisited} so far."



        button_not_visit.setOnClickListener() {

            val layoutManager = LinearLayoutManager(this)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = HillfortAdapter(hillforts?.filter {it.visit_yn}.toMutableList(), this)
            recyclerView.adapter?.notifyDataSetChanged()

        }

// create a list for left to visit



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

    override fun onHillfortClick(hillfort: HillfortModel) {
        startActivityForResult(intentFor<HillfortActivity>().putExtra("hillfort_edit", hillfort), 0)
    }


}