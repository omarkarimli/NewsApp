package com.omarkarimli.newsapp.presentation.ui.settings

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omarkarimli.newsapp.domain.repository.AuthRepository
import com.omarkarimli.newsapp.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sharedPreferences: SharedPreferences
): ViewModel() {

    val isDarkMode: MutableLiveData<Boolean> = MutableLiveData(false)
    val isNavigating: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val error: MutableLiveData<String> = MutableLiveData()

    init {
        initializeDarkModeState()
    }

    private fun initializeDarkModeState() {
        isDarkMode.value = sharedPreferences.getBoolean(Constants.DARK_MODE, false)
    }

    fun changeDarkModeState(isChecked: Boolean) {
        isDarkMode.value = isChecked

        sharedPreferences
            .edit {
                putBoolean(Constants.DARK_MODE, isChecked)
            }
    }

    fun signOutAndRedirect() {
        viewModelScope.launch {
            loading.value = true
            try {
                authRepository.signOut()
                sharedPreferences.edit { clear() }
                error.value = "Signing out..."
                isNavigating.value = true
            } catch (e: Exception) {
                error.value = "Failed to sign out: ${e.localizedMessage}"
            } finally {
                loading.value = false
            }
        }
    }
}
