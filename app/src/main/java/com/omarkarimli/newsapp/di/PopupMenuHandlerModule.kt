package com.omarkarimli.newsapp.di

import com.omarkarimli.newsapp.domain.repository.NewsRepository
import com.omarkarimli.newsapp.menu.MorePopupMenuHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PopupMenuHandlerModule {

    @Singleton
    @Provides
    fun provideMorePopupMenuHandler(repo: NewsRepository) = MorePopupMenuHandler(repo)
}