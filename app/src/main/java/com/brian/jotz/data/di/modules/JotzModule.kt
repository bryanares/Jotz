package com.brian.jotz.data.di.modules

import android.app.Application
import androidx.room.Room
import com.brian.jotz.data.database.JotzRoomDatabase
import com.brian.jotz.data.database.dao.JotzDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object JotzModule {

    @Provides
    fun provideJotzDatabase(application: Application): JotzRoomDatabase {
        return Room.databaseBuilder(
            application,
            JotzRoomDatabase::class.java, "database-name"
        )
            .fallbackToDestructiveMigration()
            .build()

    }

    @Provides
    fun provideDao(db: JotzRoomDatabase): JotzDao {
        return db.jotzDao()
    }
}