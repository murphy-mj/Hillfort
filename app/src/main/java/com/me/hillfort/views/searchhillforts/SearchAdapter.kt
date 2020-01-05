package com.me.hillfort.views.searchhillforts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.me.hillfort.R
import com.me.hillfort.models.HillfortModel
import kotlinx.android.synthetic.main.card_hillfort_wo_share.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.*
import java.util.ArrayList
import java.util.Locale


interface PlacemarkListener {
  fun onPlacemarkClick(placemark: HillfortModel)
}

class SearchAdapter constructor(private var placemarks: ArrayList<HillfortModel>,
                                   private val listener: PlacemarkListener
) : RecyclerView.Adapter<SearchAdapter.MainHolder>(),AnkoLogger {


    private val arraylist: ArrayList<HillfortModel>


    // we take a copy of the SearchView's companion Object
    // in the filter we clear the
    init {
        this.arraylist = ArrayList<HillfortModel>()
        this.arraylist.addAll(SearchHView.imageModelArrayList)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        info("In SearchAdapter  placemarks size ${placemarks.size}")
        return MainHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.card_hillfort_wo_share,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val placemark = placemarks[holder.adapterPosition]
        holder.bind(placemark, listener)
    }

    override fun getItemCount(): Int = placemarks.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(placemark: HillfortModel, listener: PlacemarkListener) {
            itemView.hillfortTitle.text = placemark.title
            itemView.hillfortDescription.text = placemark.description
            Glide.with(itemView.context).load(placemark.image).into(itemView.hillfortIcon);
            itemView.setOnClickListener { listener.onPlacemarkClick(placemark) }
        }
    }


    // Filter Class
    fun filter(charText: String) {
      //  var charText = charText
      //  charText = charText.toLowerCase(Locale.getDefault())
        SearchHView.imageModelArrayList.clear()
        info("In Filter chars to seach are   ${charText}")
        if (charText.length == 0) {
            //   SearchHView.imageModelArrayList.addAll(arraylist)
            SearchHView.imageModelArrayList.addAll(arraylist)
        } else {
            // for (wp in arraylist) {
            // if (wp.title.toLowerCase(Locale.getDefault()).contains(charText)) {
            //         SearchHView.imageModelArrayList.add(wp)
            SearchHView.imageModelArrayList.addAll(arraylist.filter {
                it.title.toLowerCase().contains(charText.toLowerCase())
            })
            info("In Filter - charText   ${charText.toLowerCase()}")
            info("In Filter - arry sive   ${SearchHView.imageModelArrayList.size}")
            info("In Filter - arry first entry   ${SearchHView.imageModelArrayList[0].title}")
        }


        notifyDataSetChanged()
    }
}

