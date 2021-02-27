package com.sk.trendinggithubrepositories.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sk.trendinggithubrepositories.repository.GithubRepository

class GithubViewModelFactory(
    private val repository: GithubRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GithubViewModel(repository) as T
    }
}