package com.omarkarimli.newsapp.di

import com.omarkarimli.newsapp.data.repository.NewsRepositoryImpl
import com.omarkarimli.newsapp.domain.repository.NewsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRepositoryModule(
        repositoryImpl: NewsRepositoryImpl
    ): NewsRepository
}