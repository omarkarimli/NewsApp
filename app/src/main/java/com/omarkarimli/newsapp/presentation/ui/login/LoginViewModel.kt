package com.omarkarimli.newsapp.presentation.ui.login

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omarkarimli.newsapp.domain.repository.AuthRepository
import com.omarkarimli.newsapp.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    val isNavigating = MutableLiveData(false)
    val loading = MutableLiveData(false)
    val error = MutableLiveData<String>()

    fun loginUserAccount(isChecked: Boolean, email: String, password: String) {
        loading.postValue(true)

        viewModelScope.launch {
            try {
                val result = authRepository.loginUserAccount(isChecked, email, password)

                if (result.user != null) {
                    // Save user preferences
                    sharedPreferences.edit()
                        .putBoolean(Constants.IS_LOGGED_KEY, isChecked)
                        .putBoolean(Constants.IS_NOTI, true)
                        .putBoolean(Constants.DARK_MODE, false)
                        .apply()

                    isNavigating.postValue(true)
                } else {
                    error.postValue("Invalid credentials. Please try again.")
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Error: ${e.message}")
                error.postValue("Error: ${e.message}")
            } finally {
                loading.postValue(false)
            }
        }
    }
}
