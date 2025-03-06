package com.omarkarimli.newsapp.di

import com.omarkarimli.newsapp.data.repository.AuthRepositoryImpl
import com.omarkarimli.newsapp.data.source.local.LocalDataSource
import com.omarkarimli.newsapp.data.source.local.LocalDataSourceImpl
import com.omarkarimli.newsapp.data.source.remote.RemoteDataSource
import com.omarkarimli.newsapp.data.source.remote.RemoteDataSourceImpl
import com.omarkarimli.newsapp.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindRemoteDataSource(
        remoteDataSourceImpl: RemoteDataSourceImpl
    ): RemoteDataSource

    @Binds
    @Singleton
    abstract fun bindLocalDataSource(
        localDataSourceImpl: LocalDataSourceImpl
    ): LocalDataSource

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
}