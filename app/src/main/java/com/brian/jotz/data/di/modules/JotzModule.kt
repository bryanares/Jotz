package com.brian.jotz.data.di.modules

import android.app.Application
import androidx.room.Room
import com.brian.jotz.data.database.db.JotzRoomDatabase
import com.brian.jotz.data.database.dao.JotzDao
import com.brian.jotz.data.repository.MainRepository
import com.brian.jotz.data.repository.MainRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore() = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideMainRepository(
        firebaseAuth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore,
        jotzDao: JotzDao
    ): MainRepository = MainRepositoryImpl(firebaseAuth, firebaseFirestore)
}