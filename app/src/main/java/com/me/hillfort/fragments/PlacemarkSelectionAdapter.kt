package com.me.hillfort.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.me.hillfort.R
//import com.squareup.picasso.Picasso
import com.me.hillfort.models.HillfortModel
//import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.card_hillfort.view.*


interface PlacemarkSelectionListener {
    fun onPlacemarkClick(placemark: HillfortModel)
}

class PlacemarkSelectionAdapter constructor(var placemarks: ArrayList<HillfortModel>,
                                  private val listener: PlacemarkSelectionListener)
    : RecyclerView.Adapter<PlacemarkSelectionAdapter.MainHolder>() {

  //  val reportAll = reportall

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
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
        holder.bind(placemark,listener)
    }

    override fun getItemCount(): Int = placemarks.size

    fun removeAt(position: Int) {
        placemarks.removeAt(position)
        notifyItemRemoved(position)
    }

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(placemark: HillfortModel, listener: PlacemarkSelectionListener) {
            itemView.tag = placemark
            //   itemView.paymentamount.text = donation.amount.toString()
            //   itemView.paymentmethod.text = donation.paymenttype
            itemView.hillfortTitle.text = placemark.title.toString()
            itemView.hillfortDescription.text = placemark.description.toString()
            itemView.setOnClickListener { listener.onPlacemarkClick(placemark) }
            // if(!reportAll)
            //    itemView.setOnClickListener { listener.onPlacemarkClick(placemark) }
//
            //          if(!placemark.profilepic.isEmpty()) {
            ///            Picasso.get().load(placemark.profilepic.toUri())
            //              //.resize(180, 180)
            //            .transform(CropCircleTransformation())
            //          .into(itemView.imageIcon)
            //}
            //    else
            //        itemView.imageIcon.setImageResource(R.mipmap.ic_launcher_homer_round)
            // }
        }
    }
}