package com.omarkarimli.newsapp.data.source.remote

import com.google.firebase.auth.AuthResult
import com.omarkarimli.newsapp.domain.models.Article
import com.omarkarimli.newsapp.domain.models.SourceX
import com.omarkarimli.newsapp.domain.models.UserData

interface RemoteDataSource {

    suspend fun fetchAllSources(): List<SourceX>

    suspend fun getArticleByUrl(url: String, query: String): Article

    suspend fun fetchAllArticles(query: String): List<Article>

    suspend fun fetchArticlesByCategory(category: String): List<Article>

    suspend fun changePassword(email: String, currentPassword: String, newPassword: String)

    suspend fun fetchUserData(): UserData?

    suspend fun loginUserAccount(isChecked: Boolean, email: String, password: String): AuthResult

    suspend fun registerNewUser(email: String, password: String): AuthResult

    suspend fun addUserToFirestore(userData: UserData)

    suspend fun updateUserInFirestore(userData: UserData)
}