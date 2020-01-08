package com.me.hillfort.activities


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils.replace
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.me.hillfort.R
import com.me.hillfort.views.login.LoginView
import com.me.hillfort.fragments.*
import com.me.hillfort.views.VIEW
import com.me.hillfort.views.editlocation.EditLocationView

import com.me.hillfort.views.map.PlacemarkMapView
import com.me.hillfort.views.hillfort.PlacemarkView
import com.me.hillfort.views.hillfortlist.PlacemarkListView
import com.me.hillfort.fragments.AboutUsFragment2
import com.me.hillfort.main.MainApp
import com.me.hillfort.models.UserModel

import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.home.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.progressDialog
import org.jetbrains.anko.toast



class Home : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener, AnkoLogger {

    lateinit var ft: FragmentTransaction
    lateinit var User : UserModel
    lateinit var app: MainApp


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)
       // setSupportActionBar(toolbar)
        app = application as MainApp
     //   if(FirebaseAuth.getInstance().currentUser != null) {
     //       User = app.pObj.findUserById(FirebaseAuth.getInstance().currentUser!!.uid.toString())!!
     //   }

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

            R.id.nav_Images ->
                navigateTo(ImagesSelectHillforts.newInstance())

            R.id.nav_aboutus ->
                navigateTo(AboutUsFragment2.newInstance())

            R.id.nav_AdminHillfort->
                navigateTo(AdminSelectUser.newInstance())

            R.id.nav_UserHillforts->
                navigateTo(UserSelectUser.newInstance())

            R.id.nav_favourites ->
                navigateTo(Favourites.newInstance())

            R.id.nav_Stats ->
                navigateTo(StatsF())

            R.id.nav_OtherStats ->
                navigateTo(StatsAdminSelectUser.newInstance())

            else -> toast("You Selected Something Else")
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

 //   override fun onCreateOptionsMenu(menu: Menu): Boolean {
 //       menuInflater.inflate(R.menu.menu_home, menu)
 //       return true
 //   }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {

//        when (item.itemId) {
         //   R.id.action_donate -> toast("You Selected Donate")
         //   R.id.action_report -> toast("You Selected Report")
  //      }
//        return super.onOptionsItemSelected(item)
//    }

 //   override fun onBackPressed() {
 // /      if (drawerLayout.isDrawerOpen(GravityCompat.START))
 //           drawerLayout.closeDrawer(GravityCompat.START)
 //       else
 //           super.onBackPressed()
 //   }


    fun StatsF() :Fragment {
        info("In Stats F I Stats F")
        //stats of Current User
        val args = Bundle()
     //   if(FirebaseAuth.getInstance().currentUser != null) {
     //          User = app.pObj.findUserById(FirebaseAuth.getInstance().currentUser!!.uid.toString())!!
     //   } else {
     //       User = UserModel("zzzz","Dummy","User")
     //   }

     //   args.putParcelable("User",User )
        //args.putString("selectedUser",user.Uuid )
        var frag = StatsFragment.newInstance()
        frag.arguments=args
        return frag
    }


    fun ImagesF() :Fragment {
        //stats of Current User
        val args = Bundle()
        //   if(FirebaseAuth.getInstance().currentUser != null) {
        //          User = app.pObj.findUserById(FirebaseAuth.getInstance().currentUser!!.uid.toString())!!
        //   } else {
        //       User = UserModel("zzzz","Dummy","User")
        //   }

        //   args.putParcelable("User",User )
        //args.putString("selectedUser",user.Uuid )
        var frag = StatsFragment.newInstance()
        frag.arguments=args
        return frag
    }






    private fun navigateTo(fragment: Fragment) {
       supportFragmentManager.beginTransaction()
           .replace(R.id.homeFrame, fragment)
            .addToBackStack("Home")
            .commit()
    }



}
