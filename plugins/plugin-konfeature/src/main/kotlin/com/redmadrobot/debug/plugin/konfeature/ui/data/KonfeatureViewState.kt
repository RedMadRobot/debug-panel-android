package com.redmadrobot.debug.plugin.konfeature.ui.data

internal data class KonfeatureViewState(
    val searchQuery: String = "",
    val collapsedConfigs: Set<String> = emptySet(),
    val configs: Map<String, KonfeatureItem.Config> = emptyMap(),
    val values: List<KonfeatureItem.Value> = emptyList(),
    val filteredItems: List<KonfeatureItem> = emptyList(),
    val editDialogState: EditDialogState? = null
) {
    val isSearchActive: Boolean
        get() = searchQuery.isNotBlank()
    val shouldShowEmptySearchItemsHint
        get() = isSearchActive && filteredItems.none { it is KonfeatureItem.Value }
}
