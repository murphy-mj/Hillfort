package com.me.hillfort.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.me.hillfort.R
//import com.squareup.picasso.Picasso
import com.me.hillfort.models.HillfortModel
import com.me.hillfort.models.UserModel
//import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.card_hillfort.view.*
import kotlinx.android.synthetic.main.card_user.view.*


interface UserListener {
    fun onUserClick(user: UserModel)
}

class UserAdapter constructor(var users: ArrayList<UserModel>,
                                  private val listener: UserListener)
    : RecyclerView.Adapter<UserAdapter.MainHolder>() {

  //  val reportAll = reportall

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent?.context).inflate(
                R.layout.card_user,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val user = users[holder.adapterPosition]
        holder.bind(user,listener)
    }

    override fun getItemCount(): Int = users.size

    fun removeAt(position: Int) {
        users.removeAt(position)
        notifyItemRemoved(position)
    }

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(user: UserModel, listener: UserListener) {
            itemView.tag = user
            //   itemView.paymentamount.text = donation.amount.toString()
            //   itemView.paymentmethod.text = donation.paymenttype
            itemView.userfirst.text = user.firstName.toString()
            itemView.userlast.text = user.lastName.toString()
            itemView.setOnClickListener { listener.onUserClick(user) }
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