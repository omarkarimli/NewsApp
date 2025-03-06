package com.omarkarimli.newsapp.data.source.remote

import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.DocumentSnapshot
import com.omarkarimli.newsapp.domain.models.Article
import com.omarkarimli.newsapp.domain.models.SourceX
import com.omarkarimli.newsapp.domain.models.UserData
import java.io.Serializable

interface RemoteDataSource {

    suspend fun fetchAllSources(): List<SourceX>

    suspend fun getArticleByUrl(url: String, query: String): Article

    suspend fun fetchAllArticles(query: String): List<Article>

    suspend fun fetchArticlesByCategory(category: String): List<Article>

    suspend fun changePassword(email: String, currentPassword: String, newPassword: String)

    suspend fun fetchUserData(): DocumentSnapshot

    suspend fun loginUserAccount(isChecked: Boolean, email: String, password: String): AuthResult

    suspend fun registerNewUser(email: String, password: String): AuthResult

    suspend fun addUserToFirestore(userData: UserData)
}