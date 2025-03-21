package com.omarkarimli.newsapp.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.omarkarimli.newsapp.data.source.local.Converters

@Entity(tableName = "article")
@TypeConverters(Converters::class)
data class Article(
    @PrimaryKey(autoGenerate = true)
    val localId: Int? = null,

    @SerializedName("author")
    val author: String?,
    @SerializedName("content")
    val content: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("publishedAt")
    val publishedAt: String?,
    @SerializedName("source")
    val source: Source?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("urlToImage")
    val urlToImage: String?,

    val isLiked: Boolean = false
)