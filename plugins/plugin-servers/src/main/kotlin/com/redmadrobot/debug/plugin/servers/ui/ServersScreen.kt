package com.redmadrobot.debug.plugin.servers.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.redmadrobot.debug.core.extension.getPlugin
import com.redmadrobot.debug.core.extension.provideViewModel
import com.redmadrobot.debug.plugin.servers.R
import com.redmadrobot.debug.plugin.servers.ServersPlugin
import com.redmadrobot.debug.plugin.servers.ServersPluginContainer
import com.redmadrobot.debug.plugin.servers.data.model.DebugServer
import com.redmadrobot.debug.uikit.components.PanelBottomSheet
import com.redmadrobot.debug.uikit.theme.DebugPanelDimensions
import com.redmadrobot.debug.uikit.theme.DebugPanelShapes
import com.redmadrobot.debug.uikit.theme.DebugPanelTheme

@Composable
internal fun ServersScreen(
    isEditMode: Boolean,
    viewModel: ServersViewModel = provideViewModel {
        getPlugin<ServersPlugin>()
            .getContainer<ServersPluginContainer>()
            .createServersViewModel(isEditMode = isEditMode)
    },
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val selectedServerMessage = stringResource(R.string.server_selected)

    LaunchedEffect(Unit) {
        viewModel.events.collect { messageEvent ->
            messageEvent?.let {
                val message = selectedServerMessage.format(messageEvent.serverName)
                snackbarHostState.showSnackbar(message = message)
            }
        }
    }

    Scaffold(
        containerColor = DebugPanelTheme.colors.background.primary,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues = paddingValues),
            contentPadding = PaddingValues(bottom = 16.dp),
        ) {
            item { if (state.isEditMode) AddServerButton(onAddClick = viewModel::onAddClicked) }
            if (state.preInstalledServers.isNotEmpty()) {
                preInstalledServersItems(
                    preInstalledServers = state.preInstalledServers,
                    onClick = { item -> viewModel.onServerClicked(debugServer = item.server) }
                )
            }

            if (state.addedServers.isNotEmpty()) {
                addedServersItems(
                    isDeleteAvailable = state.isEditMode,
                    addedServers = state.addedServers,
                    onClick = { item -> viewModel.onServerClicked(debugServer = item.server) },
                    onRemoveServerClick = viewModel::onRemoveServerClicked
                )
            }
        }
    }

    if (state.serverDialogState.show) {
        ServerBottomSheet(
            state = state.serverDialogState,
            onNameChange = viewModel::onServerNameChanged,
            onUrlChange = viewModel::onServerUrlChanged,
            onDismiss = viewModel::dismissDialogs,
            onSaveClick = viewModel::onSaveServerClicked,
        )
    }
}

private fun LazyListScope.preInstalledServersItems(
    preInstalledServers: List<ServerItemData>,
    onClick: (ServerItemData) -> Unit
) {
    item { SectionHeader(stringResource(R.string.pre_installed_servers)) }
    items(items = preInstalledServers, key = { it.server.url }) { item ->
        ServerInfoRow(
            serverName = item.server.name,
            serverUrl = item.server.url,
            isSelected = item.isSelected,
            isDeleteAvailable = false,
            onClick = { onClick.invoke(item) },
        )
    }
}

private fun LazyListScope.addedServersItems(
    isDeleteAvailable: Boolean,
    addedServers: List<ServerItemData>,
    onClick: (ServerItemData) -> Unit,
    onRemoveServerClick: (DebugServer) -> Unit
) {
    item { SectionHeader(stringResource(R.string.added_servers)) }
    items(items = addedServers, key = { it.server.url }) { item ->
        ServerInfoRow(
            serverName = item.server.name,
            serverUrl = item.server.url,
            isSelected = item.isSelected,
            isDeleteAvailable = isDeleteAvailable,
            onClick = { onClick.invoke(item) },
            onDelete = { onRemoveServerClick.invoke(item.server) },
        )
    }
}

@Composable
private fun SectionHeader(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title.uppercase(),
        style = DebugPanelTheme.typography.labelSmall,
        color = DebugPanelTheme.colors.content.tertiary,
        modifier = modifier.padding(top = 12.dp, bottom = 8.dp, start = 12.dp),
    )
}

@Composable
private fun AddServerButton(onAddClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        TextButton(
            contentPadding = PaddingValues(all = 0.dp),
            onClick = { onAddClick.invoke() }
        ) {
            Text(
                text = stringResource(R.string.add_server),
                style = DebugPanelTheme.typography.labelLarge,
                color = DebugPanelTheme.colors.content.accent,
            )
        }
    }
}

@Composable
private fun ServerInfoRow(
    serverName: String,
    serverUrl: String,
    isSelected: Boolean,
    isDeleteAvailable: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onDelete: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = DebugPanelShapes.medium)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ServerSelectionIndicator(isSelected = isSelected)
        ServerInfo(
            serverName = serverName,
            serverUrl = serverUrl,
        )
        if (isDeleteAvailable && onDelete != null) {
            ServerDeleteButton(onDelete = onDelete)
        }
    }
}

@Composable
private fun ServerSelectionIndicator(isSelected: Boolean, modifier: Modifier = Modifier) {
    val color = with(DebugPanelTheme.colors) { if (isSelected) content.teal else stroke.primary }

    Box(
        modifier = modifier
            .size(size = DebugPanelDimensions.dotSize)
            .background(color = color, shape = CircleShape),
    )
}

@Composable
private fun ServerDeleteButton(onDelete: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(
        onClick = onDelete,
        modifier = modifier.size(DebugPanelDimensions.iconSizeLarge),
    ) {
        Icon(
            painter = painterResource(R.drawable.icon_delete),
            contentDescription = null,
            tint = DebugPanelTheme.colors.content.error,
            modifier = Modifier.size(DebugPanelDimensions.iconSizeMedium),
        )
    }
}

@Composable
private fun RowScope.ServerInfo(
    serverName: String,
    serverUrl: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .weight(1f)
            .padding(start = 12.dp)
    ) {
        Text(
            text = serverName,
            style = DebugPanelTheme.typography.titleSmall,
            color = DebugPanelTheme.colors.content.primary,
        )
        Text(
            text = serverUrl,
            style = DebugPanelTheme.typography.bodySmall,
            color = DebugPanelTheme.colors.content.secondary,
            maxLines = 1,
        )
    }
}

@Composable
private fun ServerBottomSheet(
    state: ServerDialogState,
    onNameChange: (String) -> Unit,
    onUrlChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val title = if (state.editableServer != null) {
        stringResource(R.string.edit_server)
    } else {
        stringResource(R.string.add_server)
    }

    PanelBottomSheet(
        modifier = modifier,
        title = title,
        onDismiss = onDismiss,
    ) {
        ServerTextField(
            value = state.serverName,
            onValueChange = onNameChange,
            label = stringResource(R.string.name),
            isError = state.inputErrors?.nameError != null,
            errorMessage = state.inputErrors?.nameError?.let { stringResource(it) },
        )
        Spacer(modifier = Modifier.height(16.dp))
        ServerTextField(
            value = state.serverUrl,
            onValueChange = onUrlChange,
            label = stringResource(R.string.server_host_hint),
            isError = state.inputErrors?.urlError != null,
            errorMessage = state.inputErrors?.urlError?.let { stringResource(it) },
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = onSaveClick,
            modifier = Modifier.fillMaxWidth(),
            shape = DebugPanelShapes.medium,
        ) {
            Text(
                text = stringResource(R.string.save_server),
                style = DebugPanelTheme.typography.labelLarge,
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ServerTextField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(label, style = DebugPanelTheme.typography.bodyMedium) },
            isError = isError,
            singleLine = true,
            textStyle = DebugPanelTheme.typography.bodyMedium.copy(
                color = DebugPanelTheme.colors.content.primary,
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = DebugPanelTheme.colors.content.accent,
                unfocusedBorderColor = DebugPanelTheme.colors.stroke.secondary,
                focusedLabelColor = DebugPanelTheme.colors.content.accent,
                unfocusedLabelColor = DebugPanelTheme.colors.content.tertiary,
                errorBorderColor = DebugPanelTheme.colors.content.error,
                cursorColor = DebugPanelTheme.colors.content.accent,
            ),
        )
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                style = DebugPanelTheme.typography.bodySmall,
                color = DebugPanelTheme.colors.content.error,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}
