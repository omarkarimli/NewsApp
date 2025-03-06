package com.omarkarimli.newsapp.presentation.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omarkarimli.newsapp.domain.models.UserData
import com.omarkarimli.newsapp.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    val isNavigating = MutableLiveData(false)
    val loading = MutableLiveData(false)
    val error = MutableLiveData<String>()
    val succes = MutableLiveData<String>()

    fun registerNewUser(email: String, password: String, name: String, surname: String, bio: String, website: String) {
        loading.postValue(true)

        viewModelScope.launch {
            try {
                authRepository.registerNewUser(email, password)

                // Save user data to Firestore
                val userData = UserData(name, surname, bio, website)
                authRepository.addUserToFirestore(userData)

                isNavigating.postValue(true)
                succes.postValue("Registered successfully")
            } catch (e: Exception) {
                error.postValue("Error: ${e.localizedMessage}")
            } finally {
                loading.postValue(false)
            }
        }
    }
}
