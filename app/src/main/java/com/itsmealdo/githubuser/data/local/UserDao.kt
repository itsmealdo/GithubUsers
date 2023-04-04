package com.itsmealdo.githubuser.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.itsmealdo.githubuser.data.model.UserDetailResponse

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getUsers(): LiveData<List<UserDetailResponse>>

    @Query("SELECT * FROM users WHERE login = :username")
    fun getUsers(username: String): LiveData<UserDetailResponse>

    @Query("SELECT * FROM users WHERE favorite = 1")
    fun getFavoriteUsers(): LiveData<List<UserDetailResponse>>

    @Query("SELECT EXISTS(SELECT * FROM users WHERE login = :username AND favorite = 1)")
    suspend fun isFavoriteUser(username: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUsers(users: List<UserDetailResponse>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUser(user: UserDetailResponse)

    @Query("UPDATE users SET favorite = :favorite WHERE login = :username")
    suspend fun setUserFavorite(username: String, favorite: Boolean)

}