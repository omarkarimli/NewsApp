package com.omarkarimli.newsapp.data.repository

import com.omarkarimli.newsapp.data.source.local.LocalDataSource
import com.omarkarimli.newsapp.data.source.remote.RemoteDataSource
import com.omarkarimli.newsapp.domain.models.Article
import com.omarkarimli.newsapp.domain.repository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : NewsRepository {

    // Remote
    override suspend fun fetchAllArticles(query: String) = remoteDataSource.fetchAllArticles(query)

    override suspend fun getArticleByUrl(url: String, query: String) = remoteDataSource.getArticleByUrl(url, query)

    override suspend fun fetchArticlesByCategory(category: String) = remoteDataSource.fetchArticlesByCategory(category)

    override suspend fun fetchAllSources() = remoteDataSource.fetchAllSources()

    // Local
    override suspend fun getAllArticlesLocally() = localDataSource.getAllArticlesLocally()

    override suspend fun deleteArticleByTitleLocally(title: String) = localDataSource.deleteArticleByTitleLocally(title)

    override suspend fun getArticleByUrlLocally(url: String) = localDataSource.getArticleByUrlLocally(url)

    override suspend fun addArticleLocally(article: Article) = localDataSource.addArticleLocally(article)

    override suspend fun updateArticleLocally(article: Article) = localDataSource.updateArticleLocally(article)

    override suspend fun deleteAllArticlesLocally() = localDataSource.deleteAllArticlesLocally()
}