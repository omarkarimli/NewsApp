package com.omarkarimli.newsapp.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.omarkarimli.newsapp.domain.models.Article

@Database(
    entities = [Article::class],
    version = 1,
    exportSchema = false
)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
}