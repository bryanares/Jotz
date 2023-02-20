package com.brian.jotz

import android.app.Application
import com.brian.jotz.data.JotzRoomDatabase

class JotzApplication : Application() {
    val database: JotzRoomDatabase by lazy { JotzRoomDatabase.getDatabase(this) }
}