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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
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

    // THis is the Stats Page

    var hillforts = mutableListOf<HillfortModel>()

    var hillfortsNotVisited = hillforts.toMutableList()
 //   var hillfortsNotVisited2 = hillfortList.toMutableList()


    lateinit var app: MainApp
    lateinit var userEmail: String
    var numberOfHillforts:Int = 0
    var numberVisited : Int =0
    var numberWithoutDescriptions:Int = 0
//    var lastOneVisted? : Date = 1-1-2008


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)
        toolbarStats.title = title
        setSupportActionBar(toolbarStats)
        info("STATS Activity started..")
        app = application as MainApp
   //    var hillfortList = mutableListOf<HillfortModel>()



        if (intent.hasExtra("email")) {
            toast("extra extra Email")
            userEmail = intent.getStringExtra("email")
        }

        //hillforts = app.hillforts.findAll().toMutableList()
        getAllUsersHillforts2()

    //    toast("I'm Back SIZE Is EveryTHING ${hillfortList.size}")
     //  if(hillfortList.isEmpty()) {
    //        toast("its EMPTY, feck")
       // } else {
        //    numberOfHillforts = hillfortList.count()
        //    numberVisited  = hillfortList.filter {it.visit_yn}.count()

  //      hillfortList.forEach { info("${it.description}") }

          //  hillfortsNotVisited = hillforts.filter {it.visit_yn}.toMutableList()
      //  }
        textView1.text = "Hi ${userEmail}"
        textView2.text = "This is the story so far. I ${userEmail} , have ${numberOfHillforts} in my collection. I have visited ${numberVisited} so far."



        button_not_visit.setOnClickListener() {

            val layoutManager = LinearLayoutManager(this)
            recyclerView.layoutManager = layoutManager
       //     recyclerView.adapter = HillfortAdapter(hillforts.filter {!it.visit_yn}.toMutableArrayList(), this)
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



    fun getAllUsersHillforts2() {
        //toast("IN tHE MIDDLE of getallusersHillforts2")
        //   loader = createLoader(activity!!)
        //  showLoader(loader, "Downloading All Users Donations from Firebase")
      //  val hillfortsList = ArrayList<HillfortModel>()

        app.database.child("hillforts")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    info("Firebase Hillfort error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    //  hideLoader(loader)
                    val children = snapshot.children
                    children.forEach {
                        var hillfort = it.getValue<HillfortModel>(HillfortModel::class.java)

                      //  hillfortList.add(hillfort!!)
                        numberOfHillforts = numberOfHillforts + 1

                        if(hillfort!!.visit_yn) {
                            numberVisited = numberVisited+1
                        }
                        info("Vistited Hillfort : ${hillfort!!.visit_yn}")
              //          info("First Hillfort Title : ${hillfortList[0]!!.title}")
                        info("Number Hillfort  : ${ numberOfHillforts}")
                        info("Number Hillfort visted  : ${ numberVisited}")
                        //  hf.add(hillfort!!)
                       // recyclerView.adapter =
                         //   HillfortAdapter(hillfortList, this@HillfortListActivity2)
                        // HillfortAdapter(hf, this@HillfortListActivity2)


                       // recyclerView.adapter?.notifyDataSetChanged()
                        //  checkSwipeRefresh()

                        app.database.child("hillforts").removeEventListener(this)
                    }
                }
            })
    }

}
