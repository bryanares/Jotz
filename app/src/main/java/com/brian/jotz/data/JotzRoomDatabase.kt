package com.brian.jotz.data

import android.content.Context
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Jotz::class], version = 1, exportSchema = false)
abstract class JotzRoomDatabase : RoomDatabase() {

    //dao
    abstract fun jotzDao(): JotzDao

    companion object {
        @Volatile
        private var INSTANCE: JotzRoomDatabase? = null
        fun getDatabase(context: Context): JotzRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    JotzRoomDatabase::class.java,
                    "jotz_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}