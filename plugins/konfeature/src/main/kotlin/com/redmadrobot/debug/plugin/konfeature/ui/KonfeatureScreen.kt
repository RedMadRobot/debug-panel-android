package com.redmadrobot.debug.plugin.konfeaure.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.redmadrobot.debug.core.extension.getPlugin
import com.redmadrobot.debug.core.extension.provideViewModel
import com.redmadrobot.debug.plugin.konfeature.KonfeaturePlugin
import com.redmadrobot.debug.plugin.konfeature.KonfeaturePluginContainer
import com.redmadrobot.debug.plugin.konfeature.ui.EditConfigValueDialog
import com.redmadrobot.debug.plugin.konfeature.ui.KonfeatureViewModel
import com.redmadrobot.debug.plugin.konfeature.ui.data.EditDialogState
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

    var editDialogState: EditDialogState? by remember { mutableStateOf(null) }

    KonfeatureLayout(
        state = state,
        onRefreshClicked = viewModel::onRefreshClicked,
        onResetAllClicked = viewModel::onResetClicked,
        onCollapseAllClicked = viewModel::onCollapseAllClicked,
        onHeaderClicked = viewModel::onHeaderClicked,
        onEditClciked = { key, value, isDebugSource ->
            editDialogState = EditDialogState(key, value, isDebugSource)
        }
    )

    editDialogState?.let {
        EditConfigValueDialog(
            state = it,
            onValueChanged = viewModel::onValueChanged,
            onValueReset = viewModel::onValueReset,
            onDismissRequest = { editDialogState = null }
        )
    }
}

@Composable
internal fun KonfeatureLayout(
    state: KonfeatureViewState,
    onEditClciked: (String, Any, Boolean) -> Unit,
    onRefreshClicked: () -> Unit,
    onCollapseAllClicked: () -> Unit,
    onResetAllClicked: () -> Unit,
    onHeaderClicked: (String) -> Unit,
) {
    LazyColumn {
        item {
            Row(
                modifier = Modifier
                    .background(colorResource(id = CoreR.color.super_light_gray))
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Button(onClick = onRefreshClicked) {
                    Text(text = "Refresh")
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = onCollapseAllClicked) {
                    Text(text = "Collapse All")
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = onResetAllClicked) {
                    Text(text = "Reset All")
                }
            }
        }

        for (item in state.items) {
            if (item is KonfeatureItem.Config) {
                item(item.name)
                {
                    ConfigItem(
                        item = item,
                        isCollapsed = item.name in state.collapsedConfigs,
                        onHeaderClicked = onHeaderClicked
                    )
                }
            }

            if (item is KonfeatureItem.Value && item.configName !in state.collapsedConfigs) {
                item(item.key) { ValueItem(item = item, onEditClciked) }
                item { Divider(modifier = Modifier.fillMaxWidth()) }
            }
        }
    }
}


@Composable
private fun ConfigItem(
    isCollapsed: Boolean,
    item: KonfeatureItem.Config,
    onHeaderClicked: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onHeaderClicked.invoke(item.name) }
            .background(colorResource(id = CoreR.color.super_light_gray))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = item.description
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
internal fun ValueItem(item: KonfeatureItem.Value, onEditClciked: (String, Any, Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Column(Modifier.weight(1f)) {
            Text(text = item.description)
            Text(text = "key: ${item.key}")
            Text(text = "value: ${item.value}")
            Text(
                color = item.sourceColor,
                text = "source: ${item.sourceName}"
            )
        }

        if (item.editAvailable) {
            IconButton(
                modifier = Modifier.align(alignment = Alignment.CenterVertically),
                onClick = { onEditClciked.invoke(item.key, item.value, item.isDebugSource) }) {
                Icon(Icons.Outlined.Edit, contentDescription = null)
            }
        }
    }
}
