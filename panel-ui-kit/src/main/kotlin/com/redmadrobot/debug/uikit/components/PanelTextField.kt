package com.redmadrobot.debug.uikit.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.redmadrobot.debug.uikit.theme.DebugPanelTheme

private val defaultTextStyle: TextStyle
    @Composable get() = DebugPanelTheme.typography.bodyMedium.copy(
        color = DebugPanelTheme.colors.content.primary,
    )

private val defaultColors: TextFieldColors
    @Composable get() = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = DebugPanelTheme.colors.content.accent,
        unfocusedBorderColor = DebugPanelTheme.colors.stroke.secondary,
        focusedLabelColor = DebugPanelTheme.colors.content.accent,
        unfocusedLabelColor = DebugPanelTheme.colors.content.tertiary,
        errorBorderColor = DebugPanelTheme.colors.content.error,
        cursorColor = DebugPanelTheme.colors.content.accent,
    )

@Composable
public fun PanelTextField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    textStyle: TextStyle = defaultTextStyle,
    colors: TextFieldColors = defaultColors
) {
    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = label, style = DebugPanelTheme.typography.bodyMedium) },
            isError = isError,
            singleLine = true,
            keyboardOptions = keyboardOptions,
            textStyle = textStyle,
            colors = colors,
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
