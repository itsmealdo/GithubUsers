package com.itsmealdo.githubuser.data.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.itsmealdo.githubuser.data.local.UserDao
import com.itsmealdo.githubuser.data.model.UserDetailResponse
import com.itsmealdo.githubuser.data.retrofit.ApiService
import com.itsmealdo.githubuser.helper.Result
import com.itsmealdo.githubuser.helper.SettingsPreferences
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val preferences: SettingsPreferences,
    private val userApi: ApiService,
    private val userDao: UserDao
) {
    fun getUsers(): LiveData<Result<List<UserDetailResponse>>> = liveData {
        emit(Result.Loading)
        try {
            val users = userApi.getUsers()
            val userLists = users.map { user ->
                val isFavorite = userDao.isFavoriteUser(user.login)
                UserDetailResponse(
                    login = user.login,
                    htmlUrl = user.htmlUrl,
                    avatarUrl = user.avatarUrl,
                    favorite = isFavorite
                )
            }
            userDao.upsertUsers(userLists)
        } catch (e: Exception) {
            Log.d("UserRepository", "getUsers: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<UserDetailResponse>>> = userDao.getUsers().map { Result.Success(it) }
        emitSource(localData)
    }

    fun getSearchUsers(query: String): LiveData<Result<List<UserDetailResponse>>> = liveData {
        emit(Result.Loading)
        try {
            val response = userApi.getSearchUserData(query)
            val users = response.users
            emit(Result.Success(users))
        } catch (e: Exception) {
            Log.d("UserRepository", "getSearchUsers: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getUser(username: String): LiveData<Result<UserDetailResponse>> = liveData {
        emit(Result.Loading)
        try {
            val userData = userApi.getDetailUserData(username)
            val isFavorite = userDao.isFavoriteUser(userData.login)
            val user = UserDetailResponse(
                login = userData.login,
                htmlUrl = userData.htmlUrl,
                avatarUrl = userData.avatarUrl,
                name = userData.name,
                publicRepos = userData.publicRepos,
                followers = userData.followers,
                following = userData.following,
                location = userData.location,
                company = userData.company,
                favorite = isFavorite
            )
            userDao.upsertUser(user)
        } catch (e: Exception) {
            Log.d("UserRepository", "getUser: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<UserDetailResponse>> = userDao.getUsers(username).map { Result.Success(it) }
        emitSource(localData)
    }

    fun getUserFollowers(username: String): LiveData<Result<List<UserDetailResponse>>> = liveData {
        emit(Result.Loading)
        try {
            val followers = userApi.getFollowersData(username)
            emit(Result.Success(followers))
        } catch (e: Exception) {
            Log.d("UserRepository", "getUserFollowers: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getUserFollowing(username: String): LiveData<Result<List<UserDetailResponse>>> = liveData {
        emit(Result.Loading)
        try {
            val following = userApi.getFollowingData(username)
            emit(Result.Success(following))
        } catch (e: Exception) {
            Log.d("UserRepository", "getUserFollowing: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun setUserFavorite(username: String, favoriteState: Boolean) {
        userDao.setUserFavorite(username, favoriteState)
    }

    fun getFavoriteUsers(): LiveData<List<UserDetailResponse>> {
        return userDao.getFavoriteUsers()
    }

    fun getThemeSetting(): Flow<Boolean> = preferences.getThemeSetting()

    suspend fun saveThemeSetting(isNightModeActive: Boolean) {
        preferences.saveThemeSetting(isNightModeActive)
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
            preferences: SettingsPreferences,
            userApi: ApiService,
            userDao: UserDao
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(preferences, userApi, userDao)
            }.also { instance = it }
    }
}
