package com.redmadrobot.debug.plugin.konfeature.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.redmadrobot.debug.plugin.konfeature.R
import com.redmadrobot.debug.plugin.konfeature.ui.data.EditDialogState
import com.redmadrobot.debug.uikit.components.PanelDialog
import com.redmadrobot.debug.uikit.components.PanelTextField
import com.redmadrobot.debug.uikit.theme.DebugPanelShapes
import com.redmadrobot.debug.uikit.theme.DebugPanelTheme

@Composable
internal fun EditConfigValueDialog(
    state: EditDialogState,
    onValueChange: (key: String, value: Any) -> Unit,
    onValueReset: (key: String) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val initialValue = state.value
    var value by remember { mutableStateOf(state.value) }
    var isInputEmpty by remember { mutableStateOf(false) }
    val saveEnabled by remember {
        derivedStateOf { !isInputEmpty && initialValue != value }
    }

    PanelDialog(title = state.key, onDismiss = onDismissRequest, modifier = modifier) {
        when (initialValue) {
            is Boolean -> BooleanEditInput(
                value = initialValue,
                onValueChange = { value = it },
            )

            is Long -> LongEditInput(
                value = initialValue,
                onValueChange = { value = it },
                onEmptyInput = { isInputEmpty = it },
            )

            is Double -> DoubleEditInput(
                value = initialValue,
                onValueChange = { value = it },
                onEmptyInput = { isInputEmpty = it },
            )

            is String -> StringEditInput(
                value = initialValue,
                onValueChange = { value = it },
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        EditConfigValueButtons(
            state = state,
            saveEnabled = saveEnabled,
            value = value,
            onValueChange = onValueChange,
            onValueReset = onValueReset,
            onDismissRequest = onDismissRequest,
        )
    }
}

@Composable
private fun EditConfigValueButtons(
    state: EditDialogState,
    saveEnabled: Boolean,
    value: Any,
    onValueChange: (key: String, value: Any) -> Unit,
    onValueReset: (key: String) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CloseButton(onDismissRequest = onDismissRequest)
        Spacer(modifier = Modifier.weight(weight = 1f))
        if (state.isDebugSource) {
            DebugSourceButton(
                onValueReset = { onValueReset.invoke(state.key) },
                onDismissRequest = onDismissRequest
            )
        }
        SaveButton(
            saveEnabled = saveEnabled,
            onValueChange = { onValueChange.invoke(state.key, value) },
            onDismissRequest = onDismissRequest
        )
    }
}

@Composable
private fun SaveButton(
    saveEnabled: Boolean,
    onValueChange: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        modifier = modifier,
        onClick = {
            onValueChange()
            onDismissRequest()
        },
        enabled = saveEnabled,
        shape = DebugPanelShapes.medium,
    ) {
        Text(
            text = stringResource(R.string.konfeature_plugin_save),
            style = DebugPanelTheme.typography.labelLarge,
        )
    }
}

@Composable
private fun DebugSourceButton(
    onValueReset: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        modifier = modifier,
        onClick = {
            onValueReset()
            onDismissRequest()
        },
        shape = DebugPanelShapes.medium,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = DebugPanelTheme.colors.content.error,
        ),
    ) {
        Text(
            text = stringResource(R.string.konfeature_plugin_reset),
            style = DebugPanelTheme.typography.labelLarge,
        )
    }
}

@Composable
private fun CloseButton(onDismissRequest: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        modifier = modifier,
        onClick = onDismissRequest,
        shape = DebugPanelShapes.medium,
    ) {
        Text(
            text = stringResource(R.string.konfeature_plugin_close),
            style = DebugPanelTheme.typography.labelLarge,
        )
    }
}

@Composable
private fun BooleanEditInput(
    value: Boolean,
    onValueChange: (Any) -> Unit,
    modifier: Modifier = Modifier,
) {
    var checked by remember { mutableStateOf(value) }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.konfeature_plugin_edit_dialog_hint_boolean),
            style = DebugPanelTheme.typography.bodyMedium,
            color = DebugPanelTheme.colors.content.primary,
            modifier = Modifier.weight(weight = 1f),
        )
        EditConfigDialogToggle(
            checked = checked,
            onCheckedChange = { newChecked ->
                checked = newChecked
                onValueChange(newChecked)
            },
        )
    }
}

@Composable
private fun LongEditInput(
    value: Long,
    onValueChange: (Any) -> Unit,
    onEmptyInput: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    var text by remember { mutableStateOf(value.toString()) }

    PanelTextField(
        value = text,
        onValueChange = { newText ->
            val newValue = newText.toLongOrNull()
            if (newValue != null || newText.isEmpty()) {
                text = newText
                newValue?.let(onValueChange)
                onEmptyInput(newText.isEmpty())
            }
        },
        label = stringResource(R.string.konfeature_plugin_edit_dialog_hint_long),
        modifier = modifier,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    )
}

@Composable
private fun DoubleEditInput(
    value: Double,
    onValueChange: (Any) -> Unit,
    onEmptyInput: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    var text by remember { mutableStateOf(value.toBigDecimal().toPlainString()) }

    PanelTextField(
        value = text,
        onValueChange = { newText ->
            val newValue = newText.toDoubleOrNull()
            if (newValue != null || newText.isEmpty()) {
                text = newText
                newValue?.let(onValueChange)
                onEmptyInput(newText.isEmpty())
            }
        },
        label = stringResource(R.string.konfeature_plugin_edit_dialog_hint_double),
        modifier = modifier,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
    )
}

@Composable
private fun StringEditInput(
    value: String,
    onValueChange: (Any) -> Unit,
    modifier: Modifier = Modifier,
) {
    var text by remember { mutableStateOf(value) }

    PanelTextField(
        value = text,
        onValueChange = { newText ->
            text = newText
            onValueChange(newText)
        },
        label = stringResource(R.string.konfeature_plugin_edit_dialog_hint_string),
        modifier = modifier,
    )
}

@Composable
private fun EditConfigDialogToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val (trackColor, thumbOffset) = with(DebugPanelTheme.colors) {
        if (checked) content.teal to 22.dp else stroke.primary to 2.dp
    }

    Box(
        modifier = modifier
            .width(width = 44.dp)
            .height(height = 24.dp)
            .clip(shape = RoundedCornerShape(size = 12.dp))
            .background(color = trackColor)
            .clickable { onCheckedChange(!checked) },
    ) {
        Box(
            modifier = Modifier
                .padding(start = thumbOffset, top = 2.dp)
                .size(size = 20.dp)
                .background(color = Color.White, shape = CircleShape),
        )
    }
}
