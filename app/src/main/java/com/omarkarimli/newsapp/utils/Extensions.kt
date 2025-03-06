package com.omarkarimli.newsapp.utils

import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.omarkarimli.newsapp.R
import com.squareup.picasso.Picasso

fun View.visibleItem() {
    this.visibility = View.VISIBLE
}

fun View.goneItem() {
    this.visibility = View.GONE
}

fun ImageView.loadFirstImage(imageList: ArrayList<String?>?) {
    if (imageList != null) {
        imageList[0].let {
            Picasso
                .get()
                .load(it)
                .error(R.drawable.baseline_hide_image_24)
                .into(this)
        }
    } else {
        Toast.makeText(context, "No images found", Toast.LENGTH_SHORT).show()
    }
}

fun ImageView.loadFromUrlToImage(urlToImage: String?) {
    Picasso.get()
        .load(urlToImage)
        .placeholder(R.drawable.placeholder_image)
        .error(R.drawable.error_image)
        .into(this)
}
