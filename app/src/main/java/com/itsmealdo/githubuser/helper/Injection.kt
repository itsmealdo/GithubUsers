package com.itsmealdo.githubuser.helper

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.itsmealdo.githubuser.data.local.UserDatabase
import com.itsmealdo.githubuser.data.repo.UserRepository
import com.itsmealdo.githubuser.data.retrofit.ApiConfig

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val preferences = SettingsPreferences.getInstance(context.dataStore)
        val userApi = ApiConfig.getUserApi()
        val userDao = UserDatabase.getInstance(context).getUserDao()
        return UserRepository.getInstance(preferences, userApi, userDao)
    }
}