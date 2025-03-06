package com.omarkarimli.newsapp.domain.models


import com.google.gson.annotations.SerializedName

data class SourceX(
    @SerializedName("category")
    val category: String?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("language")
    val language: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("url")
    val url: String?
)