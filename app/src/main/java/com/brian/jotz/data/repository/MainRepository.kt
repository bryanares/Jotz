package com.brian.jotz.data.repository

import com.brian.jotz.data.local.User
import com.brian.jotz.data.utils.Rezults
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    suspend fun signIn(email: String, password: String): Flow<Rezults<User>>

    suspend fun signUp(email: String, password: String, name: String): Flow<Rezults<User>>

}