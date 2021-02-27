package com.sk.trendinggithubrepositories.repository

import android.content.Context
import com.google.gson.Gson
import com.sk.trendinggithubrepositories.datastore.LocalDataStore
import com.sk.trendinggithubrepositories.api.RetrofitInstance
import com.sk.trendinggithubrepositories.data.RepositoryItem
import retrofit2.Response

class GithubRepository(private val context: Context) {
    lateinit var localDataStore: LocalDataStore

    private val gson = Gson()
    suspend fun getRepository(): Response<ArrayList<RepositoryItem>> {
        val response = RetrofitInstance.api.fetchRepos()
        if (response.isSuccessful) {
            localDataStore = LocalDataStore(context)
            localDataStore.save(gson.toJson(response.body()))
        }
        return response
    }
}