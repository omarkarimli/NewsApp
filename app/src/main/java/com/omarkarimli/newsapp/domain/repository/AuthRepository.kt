package com.omarkarimli.newsapp.domain.repository

import com.google.firebase.auth.AuthResult
import com.omarkarimli.newsapp.domain.models.UserData
import java.io.Serializable

interface AuthRepository {

    suspend fun loginUserAccount(isChecked: Boolean, email: String, password: String): AuthResult

    suspend fun registerNewUser(email: String, password: String): AuthResult

    suspend fun addUserToFirestore(userData: UserData)

    suspend fun updateUserInFirestore(userData: UserData)

    suspend fun fetchUserData(): UserData?

    suspend fun changePassword(email: String, currentPassword: String, newPassword: String)

    suspend fun signOut()
}