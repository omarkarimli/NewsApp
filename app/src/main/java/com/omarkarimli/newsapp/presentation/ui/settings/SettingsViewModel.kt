package com.omarkarimli.newsapp.presentation.ui.settings

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.omarkarimli.newsapp.domain.repository.NewsRepository
import com.omarkarimli.newsapp.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repo: NewsRepository,
    private val sharedPreferences: SharedPreferences,
    private val provideAuth: FirebaseAuth
): ViewModel() {

    val isDarkMode: MutableLiveData<Boolean> = MutableLiveData(false)
    val isNavigating: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val error: MutableLiveData<String> = MutableLiveData()

    fun initializeDarkModeState() {
        isDarkMode.value = sharedPreferences.getBoolean(Constants.DARK_MODE, false)
    }

    fun changeDarkModeState(isChecked: Boolean) {
        isDarkMode.value = isChecked

        sharedPreferences
            .edit()
            .putBoolean(Constants.DARK_MODE, isChecked)
            .apply()
    }

    fun signOutAndRedirect() {
        sharedPreferences.edit().clear().apply()

        provideAuth.signOut()
        error.value = "Signing out..."
        isNavigating.value = true
    }
}
