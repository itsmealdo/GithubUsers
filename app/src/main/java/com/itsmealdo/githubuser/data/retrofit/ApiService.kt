package com.itsmealdo.githubuser.data.retrofit

import com.itsmealdo.githubuser.BuildConfig
import com.itsmealdo.githubuser.data.model.UserDetailResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import retrofit2.http.Path

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: ${BuildConfig.api_key}")
    suspend fun getSearchUserData(
        @Query("q") query: String
    ): SearchUserResponse

    @GET("users")
    @Headers("Authorization: ${BuildConfig.api_key}")
    suspend fun getUsers(): List<UserDetailResponse>

    @GET("users/{username}")
    @Headers("Authorization: ${BuildConfig.api_key}")
    suspend fun getDetailUserData(
        @Path("username") username: String
    ): UserDetailResponse

    @GET("users/{username}/following")
    @Headers("Authorization: ${BuildConfig.api_key}")
    suspend fun getFollowingData(
        @Path("username") username: String
    ): List<UserDetailResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: ${BuildConfig.api_key}")
    suspend fun getFollowersData(
        @Path("username") username: String
    ): List<UserDetailResponse>
}