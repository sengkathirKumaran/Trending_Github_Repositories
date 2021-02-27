package com.sk.trendinggithubrepositories.api

import com.sk.trendinggithubrepositories.data.RepositoryItem
import retrofit2.Response
import retrofit2.http.GET

interface GithubService {
    @GET("repositories")
    suspend fun fetchRepos(): Response<ArrayList<RepositoryItem>>
}