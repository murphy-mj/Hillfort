package com.me.hillfort.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.me.hillfort.R
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast


class LoginActivity : AppCompatActivity(), AnkoLogger{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        login_user.setOnClickListener({if(email_user.text.toString() == "Adnin@you.com" && password_user.text.toString() == "password") {
           toast("Login Successful")
            startActivityForResult(intentFor<HillfortListActivity2>(),0)

        } else {
           toast("login failed")
        }})

    }




}
