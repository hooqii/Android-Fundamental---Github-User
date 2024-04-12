package com.example.githubuser.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.githubuser.data.local.entity.FavoriteUserEntity
import com.example.githubuser.data.local.room.FavoriteUserDao
import com.example.githubuser.data.local.room.FavoriteUserDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository(application: Application) {
    private val mFavoriteUserDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUserDatabase.getDatabase(application)
        mFavoriteUserDao = db.favoriteDao()
    }

    fun getAllFavorites(): LiveData<List<FavoriteUserEntity>> = mFavoriteUserDao.getAllFavorites()

    fun getFavoriteUserByUsername(username: String?):
            LiveData<FavoriteUserEntity> = mFavoriteUserDao.getFavoriteUserByUsername(username)

    fun insert(favorite: FavoriteUserEntity) {
        executorService.execute { mFavoriteUserDao.insert(favorite) }
    }

    fun delete(favorite: FavoriteUserEntity) {
        executorService.execute { mFavoriteUserDao.delete(favorite) }
    }

}
