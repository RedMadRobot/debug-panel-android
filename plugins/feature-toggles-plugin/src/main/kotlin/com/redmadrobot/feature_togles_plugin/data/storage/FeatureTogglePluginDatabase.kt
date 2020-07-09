package com.redmadrobot.feature_togles_plugin.data.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.redmadrobot.feature_togles_plugin.data.model.FeatureToggle

@Database(
    entities = [FeatureToggle::class],
    version = 1
)
abstract class FeatureTogglePluginDatabase : RoomDatabase() {

    abstract fun getFeatureTogglesDao(): FeatureTogglesDao

    companion object {
        private const val DATABASE_NAME = "feature_toggle_plugin_db"

        fun getInstance(context: Context): FeatureTogglePluginDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                FeatureTogglePluginDatabase::class.java,
                DATABASE_NAME
            ).build()
        }

    }
}
