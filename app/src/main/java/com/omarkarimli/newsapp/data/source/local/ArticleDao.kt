package com.omarkarimli.newsapp.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.omarkarimli.newsapp.domain.models.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

    @Query("SELECT * FROM article")
    fun getAll(): Flow<List<Article>>

    @Query("DELETE FROM article")
    suspend fun deleteAll()

    @Query("DELETE FROM article WHERE title = :title")
    suspend fun deleteById(title: String)

    @Query("SELECT * FROM article WHERE url = :url")
    suspend fun getArticleByUrl(url: String): Article?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addArticle(article: Article)

    @Update
    suspend fun updateArticle(article: Article)
}