package com.redmadrobot.debug.plugin.konfeaure.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.redmadrobot.debug.core.extension.getPlugin
import com.redmadrobot.debug.core.extension.provideViewModel
import com.redmadrobot.debug.plugin.konfeature.KonfeaturePlugin
import com.redmadrobot.debug.plugin.konfeature.KonfeaturePluginContainer
import com.redmadrobot.debug.plugin.konfeature.R
import com.redmadrobot.debug.plugin.konfeature.ui.EditConfigValueDialog
import com.redmadrobot.debug.plugin.konfeature.ui.KonfeatureViewModel
import com.redmadrobot.debug.plugin.konfeature.ui.data.KonfeatureItem
import com.redmadrobot.debug.plugin.konfeature.ui.data.KonfeatureViewState
import com.redmadrobot.debug.core.R as CoreR

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun KonfeatureScreen(
    viewModel: KonfeatureViewModel = provideViewModel {
        getPlugin<KonfeaturePlugin>()
            .getContainer<KonfeaturePluginContainer>()
            .createKonfeatureViewModel()
    },
) {
    val state by viewModel.state.collectAsState(KonfeatureViewState())

    KonfeatureLayout(
        state = state,
        onRefreshClick = viewModel::onRefreshClick,
        onResetAllClick = viewModel::onResetAllClick,
        onCollapseAllClick = viewModel::onCollapseAllClick,
        onHeaderClick = viewModel::onConfigHeaderClick,
        onEditClick = viewModel::onEditClick
    )

    state.editDialogState?.let { dialogState ->
        EditConfigValueDialog(
            state = dialogState,
            onValueChange = viewModel::onValueChanged,
            onValueReset = viewModel::onValueReset,
            onDismissRequest = viewModel::onEditDialogCloseClik
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun KonfeatureLayout(
    state: KonfeatureViewState,
    onEditClick: (String, Any, Boolean) -> Unit,
    onRefreshClick: () -> Unit,
    onCollapseAllClick: () -> Unit,
    onResetAllClick: () -> Unit,
    onHeaderClick: (String) -> Unit,
) {
    LazyColumn {
        stickyHeader {
            Row(
                modifier = Modifier
                    .background(colorResource(id = CoreR.color.super_light_gray))
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Button(onClick = onRefreshClick) {
                    Text(text = stringResource(id = R.string.konfeature_plugin_refresh))
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = onCollapseAllClick) {
                    Text(text = stringResource(id = R.string.konfeature_plugin_collapse_all))
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = onResetAllClick) {
                    Text(text = stringResource(id = R.string.konfeature_plugin_reset_all))
                }
            }
        }

        state.items.forEach { item ->
            if (item is KonfeatureItem.Config) {
                item(item.name) {
                    ConfigItem(
                        item = item,
                        isCollapsed = item.name in state.collapsedConfigs,
                        onHeaderClick = onHeaderClick
                    )
                }
            }

            if (item is KonfeatureItem.Value && item.configName !in state.collapsedConfigs) {
                item(item.key) { ValueItem(item = item, onEditClick) }
                item { Divider(modifier = Modifier.fillMaxWidth()) }
            }
        }
    }
}


@Composable
private fun ConfigItem(
    isCollapsed: Boolean,
    item: KonfeatureItem.Config,
    onHeaderClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onHeaderClick.invoke(item.name) }
            .background(colorResource(id = CoreR.color.super_light_gray))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = item.description.takeIf { it.isNotEmpty() } ?: item.name
        )
        val icon = if (isCollapsed) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown

        Icon(
            imageVector = icon,
            modifier = Modifier.align(Alignment.CenterVertically),
            contentDescription = null
        )
    }
}

@Composable
internal fun ValueItem(item: KonfeatureItem.Value, onEditClick: (String, Any, Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Column(Modifier.weight(1f)) {
            Text(text = item.description)
            Text(text = stringResource(id = R.string.konfeature_plugin_item_key, item.key))
            Text(text = stringResource(id = R.string.konfeature_plugin_item_value, item.value.toString()))
            Text(
                color = item.sourceColor,
                text = stringResource(id = R.string.konfeature_plugin_item_source, item.sourceName)
            )
        }

        if (item.editAvailable) {
            IconButton(
                modifier = Modifier.align(alignment = Alignment.CenterVertically),
                onClick = { onEditClick.invoke(item.key, item.value, item.isDebugSource) }) {
                Icon(Icons.Outlined.Edit, contentDescription = null)
            }
        }
    }
}
