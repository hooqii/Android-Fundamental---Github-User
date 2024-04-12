package com.example.githubuser.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.githubuser.data.local.entity.FavoriteUserEntity

@Database(entities = [FavoriteUserEntity::class], version = 1, exportSchema = false)
abstract class FavoriteUserDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteUserDao

    companion object {
        @Volatile
        private var INSTANCE: FavoriteUserDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): FavoriteUserDatabase {
            if (INSTANCE == null) {
                synchronized(FavoriteUserDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FavoriteUserDatabase::class.java, "fav_db"
                    )
                        .build()
                }
            }
            return INSTANCE as FavoriteUserDatabase
        }
    }
}