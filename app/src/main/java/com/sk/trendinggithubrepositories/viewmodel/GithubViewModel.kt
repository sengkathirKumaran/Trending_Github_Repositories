package com.sk.trendinggithubrepositories.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sk.trendinggithubrepositories.data.RepositoryItem
import com.sk.trendinggithubrepositories.repository.GithubRepository
import kotlinx.coroutines.launch
import retrofit2.Response


class GithubViewModel(private val repository: GithubRepository) :
    ViewModel() {

    val mResponse: MutableLiveData<Response<ArrayList<RepositoryItem>>> = MutableLiveData()
    fun fetchData() {
        viewModelScope.launch {
            val response = repository.getRepository()
            mResponse.value = response
        }
    }
}
