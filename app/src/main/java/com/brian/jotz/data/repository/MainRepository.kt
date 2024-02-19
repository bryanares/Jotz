package com.brian.jotz.data.repository

import com.brian.jotz.data.local.JotItem
import com.brian.jotz.data.local.User
import com.brian.jotz.data.utils.Rezults
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    suspend fun signIn(email: String, password: String): Flow<Rezults<User>>

    suspend fun signUp(email: String, password: String, name: String): Flow<Rezults<User>>

    suspend fun addOrUpdateJotz(
        userId: String,
        jotItemId: String? = null,
        jotItem: JotItem
    ): Flow<Rezults<JotItem>>

    //fetch all jot entries
    suspend fun getAllJotItems(userId: String): Flow<Rezults<List<JotItem>>>

    //get one jot item
    suspend fun getSingleJotItem(userId: String, jotItemId: String): Flow<Rezults<JotItem>>

    //delete single jot entry
    suspend fun deleteSingleJotItem(userId: String, jotItemId: String): Flow<Rezults<List<JotItem>>>

}