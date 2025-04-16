package com.omarkarimli.newsapp.presentation.ui.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omarkarimli.newsapp.domain.models.UserData
import com.omarkarimli.newsapp.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {

    val userData = MutableLiveData<UserData?>()

    val loading = MutableLiveData(false)
    val error = MutableLiveData<String>()

    fun fetchUserData() {
        viewModelScope.launch {
            loading.value = true
            try {
                val response = repo.fetchUserData()
                if (response != null) {
                    userData.postValue(response)
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error: ${e.message}")
                error.postValue("Failed to load user data")
            } finally {
                loading.value = false
            }
        }
    }
}