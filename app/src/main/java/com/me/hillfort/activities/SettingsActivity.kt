package com.me.hillfort.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat.startActivityForResult
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
import kotlinx.android.synthetic.main.card_hillfort.*


class SettingsActivity : AppCompatActivity(), AnkoLogger {

    var setting = SettingsModel()
    lateinit var app: MainApp
    var loginUser:String = ""
    var loginUserPassword:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        toolbarsetting.title = title
        setSupportActionBar(toolbarsetting)
        info("Settings Activity started..")

        app = application as MainApp

        if (intent.hasExtra("id")) {
            //  var userID = intent.extras?.getParcelable<SettingsModel>("id")!!.toString()
            var userID = intent.getStringExtra("id")
            loginUser = app.settings.findOneName(userID).toString()
            loginUserPassword = app.settings.findOnePassword(userID).toString()
            settingEmail.setText(loginUser)
            settingPassword.setText(loginUserPassword)
            btnSetAdd.text = "UPDATE"

        }







        btnSetAdd.setOnClickListener() {
            setting.email = settingEmail.text.toString()
            setting.password = settingPassword.text.toString()
            if (setting.email.isEmpty()|| setting.password.isEmpty())  {
                toast(R.string.hint_settingsAll)
                finish()
            }
            if (loginUser == "") {
                    app.settings.createSetting(setting.copy())
                } else {
                app.settings.updateSetting(setting.copy())
            }

            info("add Button Pressed")
            setResult(AppCompatActivity.RESULT_OK)
            finish()
        }



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_settings, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.item_settings_cancel -> {
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
