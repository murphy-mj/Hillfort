package com.me.hillfort.views.searchhillforts

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.me.hillfort.R
import com.me.hillfort.main.MainApp
import com.me.hillfort.models.HillfortModel
import com.me.hillfort.views.BaseView
import com.me.hillfort.views.hillfortlist.PlacemarkAdapter
//import com.me.hillfort.views.hillfortlist.PlacemarkListener
//import kotlinx.android.synthetic.main.activity_hillfort_list.*
//import kotlinx.android.synthetic.main.activity_hillfort_list.*
import kotlinx.android.synthetic.main.activity_searchview.*
import kotlinx.android.synthetic.main.activity_searchview.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import org.jetbrains.anko.async
import org.jetbrains.anko.uiThread

class SearchHView  :  BaseView(), AnkoLogger, PlacemarkListener, SearchView.OnQueryTextListener,
    android.widget.SearchView.OnQueryTextListener {

    lateinit var presenter: SearchHPresenter
    lateinit var app: MainApp
    //lateinit var imageModelArrayList: MutableList<HillfortModel>
    lateinit var imageModelArrayList: ArrayList<HillfortModel>
    private var adapter: SearchAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        info("In Search H View create")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchview)
        //   super.init(toolbar, false)
        app = application as MainApp
        presenter = initPresenter(SearchHPresenter(this)) as SearchHPresenter
        recyclerView as RecyclerView
      //  imageModelArrayList = ArrayList()
        async {
            presenter.loadPlacemarks()
            uiThread {
            }
        }
            adapter = SearchAdapter(imageModelArrayList, this)
            recyclerView!!.adapter = adapter
            val layoutManager = LinearLayoutManager(this)
            recyclerView.layoutManager = layoutManager

            search_id.setOnQueryTextListener(this)


    }
        override fun onQueryTextSubmit(query: String): Boolean {
            return false
        }


        override fun onQueryTextChange(newText: String): Boolean {
            info("in on Query Txt Change in Search H View")
            adapter!!.filter(newText)
            return false
        }


    override fun showPlacemarks(placemarks: ArrayList<HillfortModel>) {
        info("In SHOW placemarks in Search View ")
        info("In SHOW placemarks in Search View placemarks size is  ${placemarks.size}")
        info("In SHOW placemarks in Search View imagemodel size is  ${imageModelArrayList.size}")
        recyclerView.adapter = SearchAdapter(placemarks, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }


    override fun onPlacemarkClick(placemark: HillfortModel) {
        info("On Placemark Clicked ")
       // presenter.doEditPlacemark(placemark)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        info("Search View on Activity Result ")
        presenter.loadPlacemarks()
        super.onActivityResult(requestCode, resultCode, data)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        info("Search View onOptions menu selected ")
        when (item?.itemId) {
            R.id.item_search -> presenter.doShowPlacemarkSearch()
            R.id.item_logout -> presenter.doLogout()
        }
        return super.onOptionsItemSelected(item)
    }





    companion object {
        lateinit var imageModelArrayList: ArrayList<HillfortModel>
    }


}