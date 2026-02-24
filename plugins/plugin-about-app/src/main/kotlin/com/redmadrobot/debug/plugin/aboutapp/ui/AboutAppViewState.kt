package com.redmadrobot.debug.plugin.aboutapp.ui

import androidx.compose.runtime.Immutable
import com.redmadrobot.debug.plugin.aboutapp.model.AboutAppInfo

@Immutable
internal data class AboutAppViewState(
    val appInfoList: List<AboutAppInfo>
)
