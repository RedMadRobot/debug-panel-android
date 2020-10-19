package com.redmadrobot.servers_plugin.ui

import com.xwray.groupie.kotlinandroidextensions.Item

internal data class ServersViewState(
    val preInstalledItems: List<Item>,
    val addedItems: List<Item>
)
