package com.brian.jotz.data.database.dao

import androidx.room.*
import com.brian.jotz.data.database.entities.Jotz
import kotlinx.coroutines.flow.Flow

@Dao
interface JotzDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(jotz: Jotz)

    @Delete
    suspend fun delete(jotz: Jotz)

    @Update
    suspend fun update(jotz: Jotz)

    //get specific item from id
    @Query("SELECT * FROM jotz WHERE id = :id")
    fun getItem(id : Int) : Flow<Jotz>
    //get all items
    @Query("SELECT * FROM jotz ORDER BY id DESC")
    fun getItems(): Flow<List<Jotz>>
}