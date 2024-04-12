package com.example.githubuser.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.repository.FavoriteUserRepository

class FavoriteUserViewModel(application: Application) : ViewModel() {

    private val _favoriteUserRepository = FavoriteUserRepository(application)
    fun getAllFavorites() = _favoriteUserRepository.getAllFavorites()
}
