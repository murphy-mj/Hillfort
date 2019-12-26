package com.me.hillfort.activities

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.me.hillfort.R
import com.me.hillfort.models.HillfortModel
//import kotlinx.android.synthetic.main.card_hillfort.view.*
import com.me.hillfort.helpers.readImageFromPath
import kotlinx.android.synthetic.main.activity_hillfort.view.*
import kotlinx.android.synthetic.main.card_hillfort.view.hillfortIcon
import kotlinx.android.synthetic.main.card_hillfort.view.hillfortTitle
import kotlinx.android.synthetic.main.card_hillfort.view.hillfortDescription



interface HillfortListener {
    fun onHillfortClick(hillfort: HillfortModel)
}

class HillfortAdapter constructor(private var hillforts: ArrayList<HillfortModel>,
                                   private val listener: HillfortListener) : RecyclerView.Adapter<HillfortAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(LayoutInflater.from(parent?.context).inflate(R.layout.card_hillfort, parent, false))
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val hillfort = hillforts[holder.adapterPosition]
        holder.bind(hillfort, listener)
    }

    override fun getItemCount(): Int = hillforts.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(hillfort: HillfortModel, listener: HillfortListener) {
            itemView.hillfortTitle.text = hillfort.title
            itemView.hillfortDescription.text = hillfort.description
         // itemView.hillfortIcon.setImageBitmap(readImageFromPath(itemView.context, hillfort.image))
            Log.d("adapter Hillfort image is"+hillfort.image,"Adapter")
            Glide.with(itemView.context).load(hillfort.image).into(itemView.hillfortIcon)
            itemView.setOnClickListener { listener.onHillfortClick(hillfort) }
        }
    }
}