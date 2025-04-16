package com.omarkarimli.newsapp.data.source.remote

import com.omarkarimli.newsapp.BuildConfig
import com.omarkarimli.newsapp.data.api.NewsApiService
import com.omarkarimli.newsapp.domain.models.Article
import com.omarkarimli.newsapp.domain.models.SourceX
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val apiService: NewsApiService
) : RemoteDataSource {
    private val apiKey = BuildConfig.API_KEY

    override suspend fun fetchAllSources(): List<SourceX> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getSources(apiKey).awaitResponse()
                response.body()?.sources ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    override suspend fun getArticleByUrl(url: String, query: String): Article {
        return withContext(Dispatchers.IO) {
            try {
                val articles = fetchAllArticles(query)
                articles.find { it.url == url } ?: throw NoSuchElementException("No article found with URL: $url")
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override suspend fun fetchAllArticles(query: String): List<Article> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getNews(query, apiKey).awaitResponse()
                response.body()?.articles?.filterNotNull() ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    override suspend fun fetchArticlesByCategory(category: String): List<Article> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getNewsByCategory(category, apiKey)
                response.articles?.filterNotNull() ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
}