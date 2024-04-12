package com.example.githubuser.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.githubuser.data.Result
import com.example.githubuser.data.remote.retrofit.ApiConfig
import com.example.githubuser.utils.SettingPreferences
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MainViewModel(private val pref: SettingPreferences) : ViewModel() {

    val userResult = MutableLiveData<Result>()

    fun getUser() {
        viewModelScope.launch {
            flow {
                val response = ApiConfig
                    .githubService
                    .getUser()
                emit(response)
            }.onStart {
                userResult.value = Result.Loading(true)
            }.onCompletion {
                userResult.value = Result.Loading(false)
            }.catch {
                userResult.value = Result.Error(it)
            }.collect {
                userResult.value = Result.Success(it)
            }
        }
    }

    fun getUser(query: String) {
        viewModelScope.launch {
            flow {
                val response = ApiConfig
                    .githubService
                    .searchUser(
                        mapOf(
                            "q" to query,
                            "per_page" to 10
                        )
                    )
                emit(response)
            }.onStart {
                userResult.value = Result.Loading(true)
            }.onCompletion {
                userResult.value = Result.Loading(false)
            }.catch {
                userResult.value = Result.Error(it)
            }.collect {
                userResult.value = Result.Success(it.items)
            }
        }
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

}