package com.redmadrobot.account_plugin.data.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.redmadrobot.account_plugin.data.model.DebugAccount

@Database(
    entities = [DebugAccount::class],
    version = 1
)
 internal abstract class AccountsPluginDatabase : RoomDatabase() {
    abstract fun getDebugAccountsDao(): DebugAccountDao

    companion object {
        private const val DATABASE_NAME = "accounts_plugin_db"

        fun getInstance(context: Context): AccountsPluginDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AccountsPluginDatabase::class.java,
                DATABASE_NAME
            ).build()
        }
    }
}
