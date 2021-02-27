package com.sk.trendinggithubrepositories.api

import com.sk.trendinggithubrepositories.api.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {


    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api:GithubService by lazy {
        retrofit.create(GithubService::class.java)
    }
}