package com.brian.jotz

import android.app.Application
import com.brian.jotz.data.database.JotzRoomDatabase

class JotzApplication : Application() {
    val database: JotzRoomDatabase by lazy { JotzRoomDatabase.getDatabase(this) }
}