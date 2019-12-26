package com.me.hillfort.activities


import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_hillfort_list2.*
import kotlinx.android.synthetic.main.card_hillfort.view.*
import com.me.hillfort.R
import com.me.hillfort.main.MainApp
import com.me.hillfort.models.HillfortModel
import com.me.models.SettingsModel
import org.jetbrains.anko.*
import java.util.ArrayList

class HillfortListActivity2 : AppCompatActivity(), HillfortListener, AnkoLogger {

    lateinit var app: MainApp
    var loginUser :String = ""
   // val hf: List<HillfortModel>? = null
    lateinit var userID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_list2)
        app = application as MainApp
        toolbar.title = title
        setSupportActionBar(toolbar)

        if (intent.hasExtra("id")) {
          //  var userID = intent.extras?.getParcelable<SettingsModel>("id")!!.toString()
            userID = intent.getStringExtra("id")
            loginUser = app.settings.findOneName(userID).toString()
        }
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        getAllUsersHillforts()
       // recyclerView.adapter = HillfortAdapter(app.hillforts.findAll(), this)
       // recyclerView.adapter = HillfortAdapter(hf, this)
        toast("WELCOME into listAct 2  $loginUser")
        //loadHillforts()

    }

    private fun loadHillforts() {
    //    hf = app.hillforts.findAll()
     //   showHillforts( hf!!)
    }

    fun showHillforts (hillforts: ArrayList<HillfortModel>) {
   //     recyclerView.adapter = HillfortAdapter(hillforts, this)
    //    recyclerView.adapter?.notifyDataSetChanged()
    }

    fun getAllUsersHillforts() {
        //   loader = createLoader(activity!!)
        //  showLoader(loader, "Downloading All Users Donations from Firebase")
        val hillfortsList = ArrayList<HillfortModel>()

        app.database.child("hillforts")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    info("Firebase Hillfort error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    //  hideLoader(loader)
                    val children = snapshot.children
                    children.forEach {
                        val hillfort = it.getValue<HillfortModel>(HillfortModel::class.java)
                        toast("getall users hillforts"+it)
                        hillfortsList.add(hillfort!!)
                      //  hf.add(hillfort!!)
                        recyclerView.adapter =
                             HillfortAdapter(hillfortsList, this@HillfortListActivity2)
                        // HillfortAdapter(hf, this@HillfortListActivity2)


                        recyclerView.adapter?.notifyDataSetChanged()
                        //  checkSwipeRefresh()

                        app.database.child("hillforts").removeEventListener(this)
                    }
                }
            })
    }







    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
           R.id.item_add -> {
               toast("Add Hillfort selected")
                startActivityForResult<HillfortActivity>(0)
            }

            R.id.item_logout -> {
                toast("Logout selected")
                loginUser = ""
                finish()
            }
            R.id.item_update -> {
                toast("please click on the hillfort you wish to update")
                //no action required
            }
            R.id.item_stats -> {
                toast("stats selected")
                toast("Sending you to stats $loginUser")
             //   testCall()
             //   val intent = Intent(this, StatsActivity::class.java).apply {
              //      putExtra("id", loginUser)
                 //   putExtra("data",hf as Parcelable)
              //  }
             //   startActivity(intent)
              //  startActivityForResult<Settings2Activity>(0)
                 startActivityForResult(intentFor<Settings2Activity>().putExtra("email", loginUser),0)
             //   startActivityForResult(intentFor<StatsActivity>().putExtra("data",app.hillforts.findAll() as Parcelable),0)
            }

            R.id.item_settings -> {
                toast("settings selected")
              //  startActivityForResult<SettingsActivity>(0)
                startActivityForResult(intentFor<SettingsActivity>().putExtra("id", userID),0)
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onHillfortClick(hillfort: HillfortModel) {
        startActivityForResult(intentFor<HillfortActivity>().putExtra("hillfort_edit", hillfort), 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        recyclerView.adapter?.notifyDataSetChanged()
        getAllUsersHillforts()
        super.onActivityResult(requestCode, resultCode, data)
    }

    // optional
    //   override fun onResume() {
    //       super.onResume()
    ///       getAllUsersHillforts()
    //   }



 //   fun testCall(){
      // startActivityForResult<StatsActivity>(0)
      //  startActivityForResult(intentFor<StatsActivity>().putExtra("id", loginUser),0)
  //     startActivityForResult(intentFor<Settings2Activity>().putExtra("id",loginUser),0)
      //  startActivityForResult(intentFor<SettingsActivity>().putExtra("id",loginUser),0)
 //   }


}