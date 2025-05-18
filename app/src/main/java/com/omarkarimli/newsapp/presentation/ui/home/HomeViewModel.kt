package com.omarkarimli.newsapp.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omarkarimli.newsapp.domain.models.Article
import com.omarkarimli.newsapp.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: NewsRepository
) : ViewModel() {

    private val _articles = MutableStateFlow<List<Article>>(emptyList())
    val articles: StateFlow<List<Article>> = _articles.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableSharedFlow<String>() // SharedFlow for one-time events
    val error: SharedFlow<String> = _error.asSharedFlow()

    fun fetchArticles(query: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = withContext(Dispatchers.IO) {
                    repo.fetchAllArticles(query)
                }
                _articles.value = response

                if (_articles.value.isEmpty()) {
                    _error.emit("No articles found")
                } else {
                    _loading.value = false
                }
            } catch (e: Exception) {
                _error.emit("Failed to load articles: ${e.localizedMessage}")
            }
        }
    }
}
