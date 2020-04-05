package com.redmadrobot.debug_panel.data.toggles.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "feature_toggles")
data class FeatureToggle(
    @PrimaryKey
    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "value")
    val value: Boolean
)
