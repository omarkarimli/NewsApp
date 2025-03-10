package com.omarkarimli.newsapp.data.source.local

import com.omarkarimli.newsapp.R
import com.omarkarimli.newsapp.domain.models.CategoryModel

val categoryList = mutableListOf(
    CategoryModel(id = 1, image = R.drawable.sports, name = "Sports", desc = "Sports news and live sports coverage including scores"),
    CategoryModel(id = 2, image = R.drawable.business, name = "Business", desc = "The latest breaking financial news in the world"),
    CategoryModel(id = 3, image = R.drawable.health, name = "Health", desc = "View the latest health news and explore articles"),
    CategoryModel(id = 4, image = R.drawable.science, name = "Science", desc = "Science and discoveries"),
    CategoryModel(id = 5, image = R.drawable.entertainment, name = "Entertainment", desc = "Movies, music, and more"),
    CategoryModel(id = 6, image = R.drawable.general, name = "General", desc = "General news and updates"),
    CategoryModel(id = 7, image = R.drawable.tech, name = "Technology", desc = "The latest tech news about the world's best hardware")
)