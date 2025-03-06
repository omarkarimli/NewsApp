package com.omarkarimli.newsapp.utils

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

fun getTimeAgo(publishedAt: String): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    val publishedDate = dateFormat.parse(publishedAt)
    val now = System.currentTimeMillis()

    return if (publishedDate != null) {
        val timeAgo = DateUtils.getRelativeTimeSpanString(publishedDate.time, now, DateUtils.MINUTE_IN_MILLIS)
        timeAgo.toString()
    } else {
        "Unknown"
    }
}