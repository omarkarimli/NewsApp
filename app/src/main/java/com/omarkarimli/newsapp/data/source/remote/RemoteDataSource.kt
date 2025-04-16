package com.omarkarimli.newsapp.data.source.remote

import com.omarkarimli.newsapp.domain.models.Article
import com.omarkarimli.newsapp.domain.models.SourceX

interface RemoteDataSource {

    suspend fun fetchAllSources(): List<SourceX>

    suspend fun getArticleByUrl(url: String, query: String): Article

    suspend fun fetchAllArticles(query: String): List<Article>

    suspend fun fetchArticlesByCategory(category: String): List<Article>
}