package com.omarkarimli.newsapp.domain.models

data class CategoryModel(
    val id: Int? = null,
    val image: Int?,
    val name: String? = null,
    val desc: String?,
    val isSelected: Boolean = false
)
