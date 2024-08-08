package com.redmadrobot.debug.plugin.konfeature.ui.data

internal data class KonfeatureViewState(
    val collapsedConfigs: Set<String> = emptySet(),
    val items: List<KonfeatureItem> = emptyList(),
    val editDialogState: EditDialogState? = null
)
