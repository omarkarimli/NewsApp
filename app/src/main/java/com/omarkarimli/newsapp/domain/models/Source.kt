package com.omarkarimli.newsapp.domain.models


import com.google.gson.annotations.SerializedName

data class Source(
    @SerializedName("id")
    val id: Any?,
    @SerializedName("name")
    val name: String?
)