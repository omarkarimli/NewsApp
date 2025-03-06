package com.omarkarimli.newsapp.presentation.ui.editProfile

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
class EditProfileViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {

    val navigating = MutableLiveData(false)
    val loading = MutableLiveData(false)
    val error = MutableLiveData<String>()

    fun updateUserData(userData: UserData) {
        navigating.value = false
        loading.value = true

        viewModelScope.launch {
            loading.value = true
            try {
                repo.updateUserInFirestore(userData)
                navigating.value = true
            } catch (e: Exception) {
                Log.e("EditProfileViewModel", "Error: ${e.message}")
                error.postValue("Failed to update user data")
            } finally {
                loading.value = false
            }
        }
    }
}