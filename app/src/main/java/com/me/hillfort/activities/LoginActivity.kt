package com.me.hillfort.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.me.hillfort.R
import com.me.hillfort.main.MainApp
import com.me.hillfort.models.HillfortMemStore
import com.me.hillfort.models.SettingsStore
import com.me.models.SettingsModel
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast


class LoginActivity : AppCompatActivity(), AnkoLogger{

    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        app = application as MainApp




        login_user.setOnClickListener({
          //  if(email_user.text.toString() == "Adnin@you.com" && password_user.text.toString() == "password") {
            var userL: SettingsModel? = app.settings.findOneEmail(email_user.text.toString())
            if(email_user.text.toString() == userL!!.email!!.toString() && password_user.text.toString() == userL!!.password!!.toString()){
            var userID = app.settings.findOneID(userL).toString()
           toast("Login Successful")
            startActivityForResult(intentFor<HillfortListActivity2>().putExtra("id",userID), 0)
        } else {
           toast("login failed")
        }})

        register_user.setOnClickListener {
            toast("lets start the Registration process")
            startActivityForResult(intentFor<SettingsActivity>(),0)
        }

    }




}
