package com.amanpandey.upmedia.ui

import NewsRepository.Repository
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProviderFactory(
    val app: Application,
    val newsRepository: Repository
): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return newsActivityViewModel(app,newsRepository) as T
    }
}