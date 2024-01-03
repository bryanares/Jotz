package com.brian.jotz.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.brian.jotz.data.database.dao.JotzDao
import com.brian.jotz.data.database.entities.Jotz

@Database(entities = [Jotz::class], version = 1, exportSchema = false)
abstract class JotzRoomDatabase : RoomDatabase() {

    //dao
    abstract fun jotzDao(): JotzDao
}