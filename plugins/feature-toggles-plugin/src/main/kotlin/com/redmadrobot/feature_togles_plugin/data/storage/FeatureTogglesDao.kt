package com.redmadrobot.feature_togles_plugin.data.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface FeatureTogglesDao {
    @Query("SELECT * FROM feature_toggles")
    fun getAll(): Single<List<com.redmadrobot.feature_togles_plugin.data.model.FeatureToggle>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(featureToggle: com.redmadrobot.feature_togles_plugin.data.model.FeatureToggle): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(featureToggles: List<com.redmadrobot.feature_togles_plugin.data.model.FeatureToggle>): Completable
}
