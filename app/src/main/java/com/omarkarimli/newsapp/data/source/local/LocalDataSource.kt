package com.omarkarimli.newsapp.data.source.local

import com.omarkarimli.newsapp.domain.models.Article
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun getAllArticlesLocally(): Flow<List<Article>>

    suspend fun deleteArticleByTitleLocally(title: String)

    suspend fun getArticleByUrlLocally(url: String): Article?

    suspend fun addArticleLocally(article: Article)

    suspend fun updateArticleLocally(article: Article)

    suspend fun deleteAllArticlesLocally()
}