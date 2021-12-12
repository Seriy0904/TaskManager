package dev.seriy0904.taskmanager.db

import android.app.Application
import androidx.room.Room

class TaskApp : Application() {
    val database: AppDatabase by lazy {
        Room.databaseBuilder(this, AppDatabase::class.java, "task_database")
        .build()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: TaskApp
    }
}