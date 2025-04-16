package com.omarkarimli.newsapp.domain.repository

import com.omarkarimli.newsapp.domain.models.Article
import com.omarkarimli.newsapp.domain.models.SourceX
import com.omarkarimli.newsapp.domain.models.UserData
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    // Remote
    suspend fun getArticleByUrl(url: String, query: String): Article

    suspend fun fetchAllArticles(query: String): List<Article>

    suspend fun fetchArticlesByCategory(category: String): List<Article>

    suspend fun fetchAllSources(): List<SourceX>

    // Local
    suspend fun getAllArticlesLocally(): Flow<List<Article>>

    suspend fun deleteArticleLocally(article: Article)

    suspend fun getArticleByUrlLocally(url: String): Article?

    suspend fun addArticleLocally(article: Article)

    suspend fun updateArticleLocally(article: Article)

    suspend fun deleteAllArticlesLocally()
}