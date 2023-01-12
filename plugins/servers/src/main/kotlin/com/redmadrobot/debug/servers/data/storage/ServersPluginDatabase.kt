package com.redmadrobot.debug.servers.data.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import com.redmadrobot.debug.servers.data.model.DebugServer
import com.redmadrobot.debug.servers.data.model.DebugStage

@Database(
    entities = [DebugServer::class, DebugStage::class],
    version = 2
)
@TypeConverters(DbConverters::class)
internal abstract class ServersPluginDatabase : RoomDatabase() {
    abstract fun getDebugServersDao(): DebugServersDao

    abstract fun getDebugStagesDao(): DebugStagesDao

    companion object {
        private const val DATABASE_NAME = "servers_plugin_db"

        fun getInstance(context: Context): ServersPluginDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                ServersPluginDatabase::class.java,
                DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
        }
    }
}
