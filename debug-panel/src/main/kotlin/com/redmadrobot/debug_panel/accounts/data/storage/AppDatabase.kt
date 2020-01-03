package com.redmadrobot.debug_panel.accounts.data.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.redmadrobot.debug_panel.accounts.data.model.DebugUserCredentials
import com.redmadrobot.debug_panel.accounts.data.model.DebugUserCredentialsDao

@Database(
    entities = [DebugUserCredentials::class],
    version = 1
)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun getDebugUserCredentialsDao(): DebugUserCredentialsDao

    companion object {
        private const val DATABASE_NAME = "local_storage"

        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }
}
