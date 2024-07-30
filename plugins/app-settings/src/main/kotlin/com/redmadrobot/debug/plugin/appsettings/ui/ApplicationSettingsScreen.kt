package com.redmadrobot.debug.plugin.appsettings.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import com.redmadrobot.debug.core.extension.OnLifecycleEvent
import com.redmadrobot.debug.core.extension.getPlugin
import com.redmadrobot.debug.core.extension.provideViewModel
import com.redmadrobot.debug.plugin.appsettings.AppSettingsPlugin
import com.redmadrobot.debug.plugin.appsettings.AppSettingsPluginContainer
import com.redmadrobot.debug.plugin.appsettings.R

@Composable
internal fun ApplicationSettingsScreen(
    viewModel: ApplicationSettingsViewModel = provideViewModel {
        getPlugin<AppSettingsPlugin>()
            .getContainer<AppSettingsPluginContainer>()
            .createApplicationSettingsViewModel()
    }
) {
    val state by viewModel.state.collectAsState()
    ApplicationSettingsLayout(state, onValueChanged = viewModel::updateSetting)
    OnLifecycleEvent { event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            viewModel.loadSettings()
        }
    }
}

@Composable
private fun ApplicationSettingsLayout(
    state: List<SettingItemUiModel>,
    onValueChanged: (String, Any) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(state) { itemUiModel ->
            when (itemUiModel) {
                is SettingItemUiModel.Header -> {
                    Text(
                        text = itemUiModel.header,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }

                is SettingItemUiModel.ValueItem -> {
                    ValueItem(itemUiModel, onValueChanged)
                }

                is SettingItemUiModel.BooleanItem -> {
                    BooleanItem(itemUiModel, onValueChanged)
                }
            }
        }
    }
}

@Composable
private fun ValueItem(
    itemUiModel: SettingItemUiModel.ValueItem,
    onValueChanged: (String, Any) -> Unit
) {
    var value by remember { mutableStateOf(itemUiModel.value.toString()) }
    var hasFocus by remember { mutableStateOf(false) }
    var error by remember(value) { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = itemUiModel.key,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    fontWeight = FontWeight.SemiBold,
                )
                if (hasFocus) {
                    Button(
                        onClick = {
                            try {
                                val newValue = itemUiModel.castToNeededType(value)
                                onValueChanged(itemUiModel.key, newValue)
                                focusManager.clearFocus(force = true)
                            } catch (e: Throwable) {
                                error = "Wrong data type"
                            }
                        }
                    ) {
                        Text(text = stringResource(id = R.string.save))
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = itemUiModel.value?.let { it::class.java.simpleName }
                    ?: stringResource(id = R.string.unknown_type),
                color = Color.Gray,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { hasFocus = it.hasFocus },
                value = value,
                onValueChange = { value = it },
                isError = error.isNotEmpty(),
                label = { Text(text = stringResource(id = R.string.value)) },
            )
            if (error.isNotEmpty()) {
                Text(text = error, color = Color.Red)
            }
        }
    }
}

@Composable
private fun BooleanItem(
    itemUiModel: SettingItemUiModel.BooleanItem,
    onValueChanged: (String, Boolean) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Text(
                text = itemUiModel.key,
                modifier = Modifier
                    .fillMaxWidth(),
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Boolean",
                color = Color.Gray,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Switch(
                checked = itemUiModel.value,
                onCheckedChange = { onValueChanged(itemUiModel.key, it) }
            )
        }
    }
}
