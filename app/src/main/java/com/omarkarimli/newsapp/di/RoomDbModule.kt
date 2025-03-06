package com.omarkarimli.newsapp.di

import android.content.Context
import androidx.room.Room
import com.omarkarimli.newsapp.data.source.local.ArticleDao
import com.omarkarimli.newsapp.data.source.local.ArticleDatabase
import com.omarkarimli.newsapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDbModule {

    @Provides
    @Singleton
    fun provideRoom(@ApplicationContext context: Context): ArticleDatabase {
        return Room.databaseBuilder(context, ArticleDatabase::class.java, Constants.ARTICLE_DB).build()
    }

    @Provides
    @Singleton
    fun provideArticleDao(articleDatabase: ArticleDatabase): ArticleDao {
        return articleDatabase.articleDao()
    }
}