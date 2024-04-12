package com.example.githubuser.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.data.Result
import com.example.githubuser.data.local.entity.FavoriteUserEntity
import com.example.githubuser.data.remote.retrofit.ApiConfig
import com.example.githubuser.data.repository.FavoriteUserRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class DetailUserViewModel(application: Application) : ViewModel() {

    val detailUserResult = MutableLiveData<Result>()
    val followersResult = MutableLiveData<Result>()
    val followingResult = MutableLiveData<Result>()

    private val favoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    fun getFavoriteUserByUsername(username: String?):
            LiveData<FavoriteUserEntity> =
        favoriteUserRepository.getFavoriteUserByUsername(username)

    fun insert(favorite: FavoriteUserEntity) {
        favoriteUserRepository.insert(favorite)
    }

    fun delete(favorite: FavoriteUserEntity) {
        favoriteUserRepository.delete(favorite)
    }

    fun getDetailUser(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiConfig
                    .githubService
                    .getDetail(username)
                emit(response)
            }.onStart {
                detailUserResult.value = Result.Loading(true)
            }.onCompletion {
                detailUserResult.value = Result.Loading(false)
            }.catch {
                detailUserResult.value = Result.Error(it)
            }.collect {
                detailUserResult.value = Result.Success(it)
            }
        }
    }

    fun getFollowers(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiConfig
                    .githubService
                    .getFollowers(username)
                emit(response)
            }.onStart {
                followersResult.value = Result.Loading(true)
            }.onCompletion {
                followersResult.value = Result.Loading(false)
            }.catch {
                followersResult.value = Result.Error(it)
            }.collect {
                followersResult.value = Result.Success(it)
            }
        }
    }

    fun getFollowing(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiConfig
                    .githubService
                    .getFollowing(username)
                emit(response)
            }.onStart {
                followingResult.value = Result.Loading(true)
            }.onCompletion {
                followingResult.value = Result.Loading(false)
            }.catch {
                followingResult.value = Result.Error(it)
            }.collect {
                followingResult.value = Result.Success(it)
            }
        }
    }
}