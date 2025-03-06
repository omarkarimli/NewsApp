package com.omarkarimli.newsapp.presentation.ui.bookmark

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omarkarimli.newsapp.domain.models.Article
import com.omarkarimli.newsapp.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val repo: NewsRepository
) : ViewModel() {

    private val _filteredArticles = MutableStateFlow<List<Article>>(emptyList())
    val filteredArticles: StateFlow<List<Article>> = _filteredArticles

    private val _articles = MutableStateFlow<List<Article>>(emptyList())
    val articles: StateFlow<List<Article>> = _articles

    private val _empty = MutableStateFlow(true)
    val empty: StateFlow<Boolean> = _empty

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchArticles() {
        viewModelScope.launch {
            _loading.value = true
            try {
                repo.getAllArticlesLocally().collectLatest { articlesList ->
                    Log.d("BookmarkViewModel", "Received articles: $articlesList")
                    _articles.value = articlesList
                    _filteredArticles.value = articlesList
                    updateEmptyState()
                    _loading.value = false
                }
            } catch (e: Exception) {
                Log.e("BookmarkViewModel", "Error: ${e.message}")
                _error.value = "Failed to load articles"
                _loading.value = false
            }
        }
    }

    fun updateFilteredArticles(query: String) {
        val filteredList = if (query.isNotEmpty()) {
            _articles.value.filter { it.title?.contains(query, ignoreCase = true) == true }
        } else {
            _articles.value
        }

        _filteredArticles.value = filteredList
        updateEmptyState()
    }

    private fun updateEmptyState() {
        _empty.value = _filteredArticles.value.isEmpty()
    }

    fun clearError() {
        _error.value = null
    }
}
