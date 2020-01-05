package com.me.hillfort.views.hillfortlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.me.hillfort.R
import com.me.hillfort.models.HillfortModel
import kotlinx.android.synthetic.main.card_hillfort.view.*
import com.me.hillfort.helpers.readImageFromPath
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

interface PlacemarkListener {
  fun onPlacemarkClick(placemark: HillfortModel)
}
interface FavouriteListener {
    fun onFavouriteClick(placemark: HillfortModel)
}
class PlacemarkAdapter constructor(private var placemarks: List<HillfortModel>,
                                   private val listener: PlacemarkListener,  private val listenerFav: FavouriteListener
) : RecyclerView.Adapter<PlacemarkAdapter.MainHolder>(),AnkoLogger {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        info("In PlacemarkAdapter  placemarks size ${placemarks.size}")
        return MainHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.card_hillfort,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val placemark = placemarks[holder.adapterPosition]
        holder.bind(placemark, listener, listenerFav)
    }

    override fun getItemCount(): Int = placemarks.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(
            placemark: HillfortModel,
            listener: PlacemarkListener,
            listenerFav: FavouriteListener
        ) {
            itemView.hillfortTitle.text = placemark.title
            itemView.hillfortDescription.text = placemark.description
            Glide.with(itemView.context).load(placemark.image).into(itemView.hillfortIcon);
            itemView.setOnClickListener { listener.onPlacemarkClick(placemark) }
            itemView.toggleButton.setOnCheckedChangeListener { buttonView, isChecked ->
                listenerFav.onFavouriteClick(placemark)
            }

            }
    }
}