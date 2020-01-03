package com.me.hillfort.activities


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.me.hillfort.R
import com.me.hillfort.views.login.LoginView
import com.me.hillfort.fragments.*
import com.me.hillfort.views.VIEW
import com.me.hillfort.views.editlocation.EditLocationView

import com.me.hillfort.views.map.PlacemarkMapView
import com.me.hillfort.views.hillfort.PlacemarkView
import com.me.hillfort.views.hillfortlist.PlacemarkListView
import com.me.hillfort.fragments.AboutUsFragment2

import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.home.*
import org.jetbrains.anko.progressDialog
import org.jetbrains.anko.toast

class Home : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    lateinit var ft: FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)
        setSupportActionBar(toolbar)

        // this is for the Floating icon at the bottom of screen
      //  fab.setOnClickListener { view ->
      //      Snackbar.make(view, "Replace with your own action",
      //          Snackbar.LENGTH_LONG).setAction("Action", null).show()
      //  }

        navView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        ft = supportFragmentManager.beginTransaction()

        val fragment = AboutUsFragment.newInstance()

        ft.replace(R.id.homeFrame, fragment)
        ft.commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_login -> {
               startActivity(Intent(this, LoginView::class.java))
                //navigateTo(ReportFragment.newInstance())
            }
            R.id.nav_report ->
             //   navigateTo(ReportBasicFrag.newInstance())
                startActivity(Intent(this, PlacemarkListView::class.java))
             //   navigateTo(PlacemarkFragment.newInstance())

            R.id.nav_aboutus ->
                navigateTo(AboutUsFragment2.newInstance())

            R.id.nav_AdminHillfort->
                navigateTo(AdminSelectUser.newInstance())

            R.id.nav_favourites ->
                navigateTo(Favourites.newInstance())

            else -> toast("You Selected Something Else")
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
         //   R.id.action_donate -> toast("You Selected Donate")
         //   R.id.action_report -> toast("You Selected Report")
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

    private fun navigateTo(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.homeFrame, fragment)
            .addToBackStack(null)
            .commit()
    }



}
