package com.omarkarimli.newsapp.presentation.ui.article

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import com.omarkarimli.newsapp.data.source.remote.RemoteDataSourceImpl
import com.omarkarimli.newsapp.domain.models.Article
import com.omarkarimli.newsapp.domain.repository.NewsRepository
import com.omarkarimli.newsapp.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val repo: NewsRepository
) : ViewModel() {

    val article = MutableLiveData<Article>()

    val loading = MutableLiveData(false)
    val error = MutableLiveData<String>()

    fun fetchArticle(url: String, query: String) {
        viewModelScope.launch {
            loading.value = true
            try {
                val response = repo.getArticleByUrl(url, query)
                article.postValue(response)
            } catch (e: Exception) {
                Log.e("ArticleViewModel", "Error: ${e.message}")
                error.postValue("Failed to load article")
            } finally {
                loading.value = false
            }
        }
    }
}
