package com.redmadrobot.feature_togles_plugin.data.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.redmadrobot.feature_togles_plugin.data.model.FeatureToggle

@Dao
interface FeatureTogglesDao {
    @Query("SELECT * FROM feature_toggles")
    suspend fun getAll(): List<FeatureToggle>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(featureToggle: FeatureToggle)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(featureToggles: List<FeatureToggle>)
}
