package com.omarkarimli.newsapp.presentation.ui.explore

import androidx.lifecycle.ViewModel
import com.omarkarimli.newsapp.data.source.remote.RemoteDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : ViewModel() {
}