package com.omarkarimli.newsapp.data.source.local

import com.omarkarimli.newsapp.domain.models.Article
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val articleDao: ArticleDao
) : LocalDataSource {

    override suspend fun getAllArticlesLocally() = articleDao.getAll()

    override suspend fun deleteArticleLocally(article: Article) = articleDao.delete(article)

    override suspend fun getArticleByUrlLocally(url: String) = articleDao.getArticleByUrl(url)

    override suspend fun addArticleLocally(article: Article) = articleDao.addArticle(article)

    override suspend fun updateArticleLocally(article: Article) = articleDao.updateArticle(article)

    override suspend fun deleteAllArticlesLocally() = articleDao.deleteAll()
}