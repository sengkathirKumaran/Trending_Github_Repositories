package com.sk.trendinggithubrepositories.repository

import android.content.Context
import com.google.gson.Gson
import com.sk.trendinggithubrepositories.api.RetrofitInstance
import com.sk.trendinggithubrepositories.data.RepositoryItem
import com.sk.trendinggithubrepositories.datastore.LocalDataStore
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

class GithubRepository(private val context: Context) {
    lateinit var localDataStore: LocalDataStore
    private lateinit var response: Response<ArrayList<RepositoryItem>>

    private val gson = Gson()
    suspend fun getRepository(): Response<ArrayList<RepositoryItem>> {
        try {
            response = RetrofitInstance.api.fetchRepos()
            if (response.isSuccessful) {
                localDataStore = LocalDataStore(context)
                localDataStore.save(gson.toJson(response.body()))
            }
        } catch (ex: SocketTimeoutException) {
            ex.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return response
    }
}