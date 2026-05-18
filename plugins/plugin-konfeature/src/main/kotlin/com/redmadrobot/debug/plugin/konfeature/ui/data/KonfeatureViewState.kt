package com.redmadrobot.debug.plugin.konfeature.ui.data

internal data class KonfeatureViewState(
    val searchQuery: String = "",
    val collapsedConfigs: Set<String> = emptySet(),
    val configs: Map<String, KonfeatureItem.Config> = emptyMap(),
    val values: List<KonfeatureItem.Value> = emptyList(),
    val filteredItems: List<KonfeatureItem> = emptyList(),
    val matchingKeys: Set<String> = emptySet(),
    val editDialogState: EditDialogState? = null
) {
    val isSearchActive: Boolean
        get() = searchQuery.isNotBlank()
    val shouldShowEmptySearchItemsHint: Boolean
        get() {
            val isNotMatchingKeys = matchingKeys.none { key ->
                key.startsWith(prefix = KonfeatureItem.ITEM_KEY_PREFIX_VALUE)
            }
            return isSearchActive && isNotMatchingKeys
        }
}
