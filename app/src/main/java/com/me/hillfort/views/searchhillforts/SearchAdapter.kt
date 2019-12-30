package com.me.hillfort.views.searchhillforts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.me.hillfort.R
import com.me.hillfort.views.searchhillforts.SearchHView
import com.me.hillfort.models.HillfortModel
import kotlinx.android.synthetic.main.card_hillfort.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.*
import java.util.ArrayList
import java.util.Locale


interface PlacemarkListener {
  fun onPlacemarkClick(placemark: HillfortModel)
}

class SearchAdapter constructor(private var placemarks: List<HillfortModel>,
                                   private val listener: PlacemarkListener
) : RecyclerView.Adapter<SearchAdapter.MainHolder>(),AnkoLogger {


    private val arraylist: ArrayList<HillfortModel>

    init {

        //inflater = LayoutInflater.from(ctx)
        this.arraylist = ArrayList<HillfortModel>()
        this.arraylist.addAll(SearchHView.imageModelArrayList)
    }



  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
      info("In PlacemarkAdapter  placemarks size ${placemarks.size}")
    return MainHolder(
        LayoutInflater.from(parent?.context).inflate(
            R.layout.card_hillfort,
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
        var charText = charText
        charText = charText.toLowerCase(Locale.getDefault())
       // SearchHView.imageModelArrayList.clear()
       // SearchHView.
        if (charText.length == 0) {
            SearchHView.imageModelArrayList.addAll(arraylist)
        } else {
            for (wp in arraylist) {
                if (wp.title.toLowerCase(Locale.getDefault()).contains(charText)) {
                    SearchHView.imageModelArrayList.add(wp)
                }
            }
        }
        notifyDataSetChanged()
    }


}