package com.omarkarimli.newsapp.data.source.remote

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.omarkarimli.newsapp.NewsApiService
import com.omarkarimli.newsapp.domain.models.Article
import com.omarkarimli.newsapp.domain.models.SourceX
import com.omarkarimli.newsapp.domain.models.UserData
import com.omarkarimli.newsapp.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val apiService: NewsApiService,
    private val provideAuth: FirebaseAuth,
    private val provideFirestore: FirebaseFirestore,
) : RemoteDataSource {

    override suspend fun fetchAllSources(): List<SourceX> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getSources(Constants.API_KEY).awaitResponse()
                response.body()?.sources ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    override suspend fun getArticleByUrl(url: String, query: String): Article {
        return withContext(Dispatchers.IO) {
            try {
                val articles = fetchAllArticles(query)
                articles.find { it.url == url } ?: throw NoSuchElementException("No article found with URL: $url")
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override suspend fun fetchAllArticles(query: String): List<Article> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getNews(query, Constants.API_KEY).awaitResponse()
                response.body()?.articles?.filterNotNull() ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    override suspend fun fetchArticlesByCategory(category: String): List<Article> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getNewsByCategory(category, Constants.API_KEY)
                response.articles?.filterNotNull() ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

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

    override suspend fun fetchUserData(): DocumentSnapshot =
        provideFirestore
            .collection(Constants.USERS)
            .document(provideAuth.currentUser?.uid ?: "error")
            .get()
            .await()

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
        provideFirestore
            .collection(Constants.USERS)
            .document(uid)
            .set(userData)
            .await()
    }
}