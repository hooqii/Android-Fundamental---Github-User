package com.example.githubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.githubuser.data.local.entity.FavoriteUserEntity

@Dao
interface FavoriteUserDao {
    @Query("SELECT * FROM FavoriteUserEntity WHERE username = :username")
    fun getFavoriteUserByUsername(username: String?): LiveData<FavoriteUserEntity>

    @Query("SELECT * from FavoriteUserEntity")
    fun getAllFavorites(): LiveData<List<FavoriteUserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favorite: FavoriteUserEntity)

    @Update
    fun update(favorite: FavoriteUserEntity)

    @Delete
    fun delete(favorite: FavoriteUserEntity)
}