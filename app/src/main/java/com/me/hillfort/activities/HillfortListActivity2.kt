package com.me.hillfort.activities


import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_hillfort_list2.*
import kotlinx.android.synthetic.main.card_hillfort.view.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult
import com.me.hillfort.R
import com.me.hillfort.main.MainApp
import com.me.hillfort.models.HillfortModel
import com.me.models.SettingsModel
import org.jetbrains.anko.toast

class HillfortListActivity2 : AppCompatActivity(), HillfortListener {

    lateinit var app: MainApp
    var loginUser :String = ""
    var hf:List<HillfortModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_list2)
        app = application as MainApp
        toolbar.title = title
        setSupportActionBar(toolbar)

        if (intent.hasExtra("id")) {
          //  var userID = intent.extras?.getParcelable<SettingsModel>("id")!!.toString()
            var userID = intent.getStringExtra("id")
            loginUser = app.settings.findOneName(userID).toString()
        }
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = HillfortAdapter(app.hillforts.findAll(), this)
        toast("Welcome into listAct 2  $loginUser")
        loadHillforts()
    }

    private fun loadHillforts() {
        hf = app.hillforts.findAll()
        showHillforts( hf!!)
    }

    fun showHillforts (hillforts: List<HillfortModel>) {
        recyclerView.adapter = HillfortAdapter(hillforts, this)
        recyclerView.adapter?.notifyDataSetChanged()
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
                startActivityForResult<SettingsActivity>(0)
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onHillfortClick(hillfort: HillfortModel) {
        startActivityForResult(intentFor<HillfortActivity>().putExtra("hillfort_edit", hillfort), 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
      //  recyclerView.adapter?.notifyDataSetChanged()
        loadHillforts()
        super.onActivityResult(requestCode, resultCode, data)
    }

 //   fun testCall(){
      // startActivityForResult<StatsActivity>(0)
      //  startActivityForResult(intentFor<StatsActivity>().putExtra("id", loginUser),0)
  //     startActivityForResult(intentFor<Settings2Activity>().putExtra("id",loginUser),0)
      //  startActivityForResult(intentFor<SettingsActivity>().putExtra("id",loginUser),0)
 //   }


}