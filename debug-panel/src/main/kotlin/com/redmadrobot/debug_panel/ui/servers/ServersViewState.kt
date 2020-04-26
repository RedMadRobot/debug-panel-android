package com.redmadrobot.debug_panel.ui.servers

import com.xwray.groupie.kotlinandroidextensions.Item

data class ServersViewState(
    val preInstalledItems: List<Item>,
    val addedItems: List<Item>
)
