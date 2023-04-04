package com.itsmealdo.githubuser.data.local

import android.content.Context
import androidx.room.*
import com.itsmealdo.githubuser.data.model.UserDetailResponse

@Database(
    entities = [UserDetailResponse::class],
    version = 10
)

abstract class UserDatabase: RoomDatabase() {
    abstract fun getUserDao(): UserDao

    companion object {
        @Volatile
        private var instance: UserDatabase? = null

        fun getInstance(context: Context): UserDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java, "github_users.db"
                ).fallbackToDestructiveMigration().build()
            }
    }
}