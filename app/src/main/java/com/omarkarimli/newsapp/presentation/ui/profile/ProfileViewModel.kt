package com.omarkarimli.newsapp.presentation.ui.profile

import androidx.lifecycle.ViewModel
import com.omarkarimli.newsapp.data.source.remote.RemoteDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : ViewModel() {
}