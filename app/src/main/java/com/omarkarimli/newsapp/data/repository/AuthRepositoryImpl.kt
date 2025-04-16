package com.omarkarimli.newsapp.data.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.omarkarimli.newsapp.data.source.remote.RemoteDataSource
import com.omarkarimli.newsapp.domain.models.UserData
import com.omarkarimli.newsapp.domain.repository.AuthRepository
import com.omarkarimli.newsapp.utils.Constants
import kotlinx.coroutines.tasks.await
import java.io.Serializable
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val provideAuth: FirebaseAuth,
    private val provideFirestore: FirebaseFirestore
): AuthRepository {

    override suspend fun changePassword(email: String, currentPassword: String, newPassword: String) {
        val user = provideAuth.currentUser ?: FirebaseAuth.getInstance().currentUser
        val credential = EmailAuthProvider.getCredential(email, currentPassword)

        try {
            if (user != null) {
                user.reauthenticate(credential).await() // Re-authenticate user
                user.updatePassword(newPassword).await() // Update password
            } else {
                throw Exception("User is not authenticated")
            }
        } catch (e: Exception) {
            throw e // Handle exception appropriately
        }
    }

    override suspend fun signOut() {
        provideAuth.signOut()
    }

    override suspend fun fetchUserData(): UserData? {
        val uid = provideAuth.currentUser?.uid ?: "error"
        val snapshot = provideFirestore
            .collection(Constants.USERS)
            .document(uid)
            .get()
            .await()

        return snapshot.toObject(UserData::class.java)
    }

    override suspend fun loginUserAccount(isChecked: Boolean, email: String, password: String): AuthResult =
        provideAuth
            .signInWithEmailAndPassword(email, password)
            .await()

    override suspend fun registerNewUser(email: String, password: String): AuthResult =
        provideAuth
            .createUserWithEmailAndPassword(email, password)
            .await()

    override suspend fun addUserToFirestore(userData: UserData) {
        val uid = provideAuth.currentUser?.uid ?: "error"
        val userMap = mapOf(
            Constants.NAME to userData.name,
            Constants.SURNAME to userData.surname,
            Constants.BIO to userData.bio,
            Constants.WEBSITE to userData.website
        )
        provideFirestore
            .collection(Constants.USERS)
            .document(uid)
            .set(userMap)
            .await()
    }

    override suspend fun updateUserInFirestore(userData: UserData) {
        val uid = provideAuth.currentUser?.uid ?: "error"
        val userMap = mapOf(
            Constants.NAME to userData.name,
            Constants.SURNAME to userData.surname,
            Constants.BIO to userData.bio,
            Constants.WEBSITE to userData.website
        )
        provideFirestore
            .collection(Constants.USERS)
            .document(uid)
            .set(userMap, SetOptions.merge())
            .await()
    }
}