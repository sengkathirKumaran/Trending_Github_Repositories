package com.sk.trendinggithubrepositories.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.first


class LocalDataStore(context: Context) {

    private val dataStore = context.createDataStore(name = "localDataStore")


    companion object {
        val LOCAL_DATA_LIST = preferencesKey<String>("localDataList")

    }

    suspend fun save(value: String) {
        dataStore.edit {
            it[LOCAL_DATA_LIST] = value
        }
    }


    suspend fun read(): String? {
        val preferences = dataStore.data.first()
        return preferences[LOCAL_DATA_LIST]
    }
}