package com.brian.jotz.data.di.modules

import androidx.room.Dao
import com.brian.jotz.data.database.dao.JotzDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.internal.managers.ApplicationComponentManager

@Module
@InstallIn(ApplicationComponentManager::class)
object JotzModule {

    @Provides
    fun provideDao (dao : JotzDao) : JotzDao {
        return dao
    }
}