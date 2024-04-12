package com.example.githubuser.data.remote.retrofit

import com.example.githubuser.data.remote.model.DetailResponse
import com.example.githubuser.data.remote.model.UserResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface ApiService {

    @GET("users")
    suspend fun getUser(): MutableList<UserResponse.Users>

    @GET("users/{username}")
    suspend fun getDetail(@Path("username") username : String): DetailResponse

    @GET("users/{username}/followers")
    suspend fun getFollowers(@Path("username") username : String): MutableList<UserResponse.Users>

    @GET("users/{username}/following")
    suspend fun getFollowing(@Path("username") username : String): MutableList<UserResponse.Users>

    @GET("search/users")
    @JvmSuppressWildcards
    suspend fun searchUser(@QueryMap params: Map<String, Any>): UserResponse
}