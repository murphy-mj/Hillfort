package com.me.hillfort.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
//import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.me.hillfort.R
import com.me.hillfort.models.ImageModel
import kotlinx.android.synthetic.main.card_image.view.*



interface ImageListener {
    fun onImageClick(image: ImageModel)
}

class ImageAdapter constructor(var imagesP: ArrayList<ImageModel>,
                                  private val listener: ImageListener)
    : RecyclerView.Adapter<ImageAdapter.MainHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent?.context).inflate(
                R.layout.card_image,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val imageP = imagesP[holder.adapterPosition]
        holder.bind(imageP,listener)
    }

    override fun getItemCount(): Int = imagesP.size

    fun removeAt(position: Int) {
        imagesP.removeAt(position)
        notifyItemRemoved(position)
    }

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(imageP: ImageModel, listener: ImageListener) {
            itemView.tag = imageP
            itemView.imageName.text = imageP.name.toString()
            Glide.with(itemView.context).load(imageP.url).into(itemView.imageIcon);
            itemView.setOnClickListener { listener.onImageClick(imageP) }

        }
    }
}



// to be implemented at a later date, takenn from Donations


//import com.squareup.picasso.Picasso
//import jp.wasabeef.picasso.transformations.CropCircleTransformation
// Picasso.with(this.itemView.context)
//     .load(imageP.url)
//     .into(itemView.imageIcon)

//  .error(R.drawable.common_google_signin_btn_icon_dark)

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