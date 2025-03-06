package com.omarkarimli.newsapp.presentation.ui.trending

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
class TrendingViewModel @Inject constructor(
    private val repo: NewsRepository
) : ViewModel() {

    val articles = MutableLiveData<List<Article>>()

    val loading = MutableLiveData(false)
    val error = MutableLiveData<String>()

    init {
        fetchArticles(Constants.EVERYTHING)
    }

    fun fetchArticles(query: String) {
        viewModelScope.launch {
            loading.value = true
            try {
                val response = repo.fetchAllArticles(query)
                articles.postValue(response)
            } catch (e: Exception) {
                Log.e("TrendingViewModel", "Error: ${e.message}")
                error.postValue("Failed to load articles")
            } finally {
                loading.value = false
            }
        }
    }
}
