package com.omarkarimli.newsapp.utils

import android.text.format.DateUtils
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.omarkarimli.newsapp.R
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Locale

fun View.visibleItem() {
    this.visibility = View.VISIBLE
}

fun View.goneItem() {
    this.visibility = View.GONE
}

fun ImageView.loadFromUrlToImage(urlToImage: String?) {
    Picasso.get()
        .load(urlToImage)
        .placeholder(R.drawable.placeholder_image)
        .error(R.drawable.error_image)
        .into(this)
}

fun String.getTimeAgo(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    val publishedDate = dateFormat.parse(this)
    val now = System.currentTimeMillis()

    return if (publishedDate != null) {
        val timeAgo = DateUtils.getRelativeTimeSpanString(publishedDate.time, now, DateUtils.MINUTE_IN_MILLIS)
        timeAgo.toString()
    } else {
        "Unknown"
    }
}
