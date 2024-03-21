package com.example.skillcinema

import android.app.Application
import androidx.room.Room
import com.example.skillcinema.data.entity.AppDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    private lateinit var db: AppDatabase

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "db"
        ).fallbackToDestructiveMigration()
            .build()

    }

}


