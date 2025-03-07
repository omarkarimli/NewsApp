package com.omarkarimli.newsapp.presentation.ui.explore

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omarkarimli.newsapp.domain.models.Article
import com.omarkarimli.newsapp.domain.models.CategoryModel
import com.omarkarimli.newsapp.domain.repository.NewsRepository
import com.omarkarimli.newsapp.utils.categoryList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val repo: NewsRepository
) : ViewModel() {

    val categories = MutableLiveData<List<CategoryModel>>()
    val articles = MutableLiveData<List<Article>>()

    val loading = MutableLiveData(false)
    val error = MutableLiveData<String>()

    fun fetchArticles(query: String) {
        viewModelScope.launch {
            loading.value = true
            try {
                val response = repo.fetchAllArticles(query)
                articles.value = response
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Error: ${e.message}")
                error.postValue("Failed to load articles")
            } finally {
                loading.value = false
            }
        }
    }

    fun fetchCategories() {
        categories.value = categoryList
    }
}