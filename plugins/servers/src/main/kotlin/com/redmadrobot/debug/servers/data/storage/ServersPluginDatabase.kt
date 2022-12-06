package com.redmadrobot.debug.servers.data.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.redmadrobot.debug.servers.data.model.DebugServer

@Database(
    entities = [DebugServer::class],
    version = 1
)
internal abstract class ServersPluginDatabase : RoomDatabase() {
    abstract fun getDebugServersDao(): DebugServersDao

    companion object {
        private const val DATABASE_NAME = "servers_plugin_db"

        fun getInstance(context: Context): ServersPluginDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                ServersPluginDatabase::class.java,
                DATABASE_NAME
            ).build()
        }
    }
}
