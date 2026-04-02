package com.redmadrobot.debug.core.data.storage.model

import com.redmadrobot.debug.uikit.theme.model.ThemeMode
import kotlinx.serialization.Serializable

@Serializable
internal data class ThemeData(
    val themeMode: ThemeMode = ThemeMode.System,
)
