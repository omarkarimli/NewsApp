package com.omarkarimli.newsapp.presentation.ui.bookmark

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omarkarimli.newsapp.domain.models.Article
import com.omarkarimli.newsapp.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val repo: NewsRepository
) : ViewModel() {

    val filteredArticles = MutableStateFlow<List<Article>>(emptyList())
    val articles = MutableStateFlow<List<Article>>(emptyList())

    val loading = MutableStateFlow(false)
    val error = MutableStateFlow<String?>(null)

    fun fetchArticles() {
        viewModelScope.launch {
            loading.value = true
            try {
                // Collect Flow and update MutableStateFlow for articles
                repo.getAllArticlesLocally().collectLatest { articlesList ->
                    Log.d("BookmarkViewModel", "Received articles: $articlesList")
                    articles.value = articlesList
                    filteredArticles.value = articlesList

                    loading.value = false
                }
            } catch (e: Exception) {
                Log.e("BookmarkViewModel", "Error: ${e.message}")
                error.value = "Failed to load articles"
            }
        }
    }
}
