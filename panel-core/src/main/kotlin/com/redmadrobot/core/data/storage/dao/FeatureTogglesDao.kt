package com.redmadrobot.core.data.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.redmadrobot.core.data.storage.entity.FeatureToggle
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface FeatureTogglesDao {
    @Query("SELECT * FROM feature_toggles")
    fun getAll(): Single<List<FeatureToggle>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(featureToggle: FeatureToggle): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(featureToggles: List<FeatureToggle>): Completable
}
