package com.redmadrobot.variable_plugin.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.redmadrobot.variable_plugin.R
import com.redmadrobot.variable_plugin.databinding.FragmentVariableBinding
import com.redmadrobot.variable_plugin.ui.model.VariableEvent
import com.redmadrobot.variable_plugin.ui.model.VariableItem
import com.redmadrobot.variable_plugin.ui.model.VariableSetting
import com.redmadrobot.variable_plugin.ui.model.VariableSettings
import com.redmadrobot.variable_plugin.ui.recycler.VariableAdapter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

private val modifiers: MutableStateFlow<MutableMap<String, VariableItem>> = MutableStateFlow(
    mutableMapOf()
)

private val settings: MutableMap<String, VariableSettings> = mutableMapOf()

internal class VariableFragment : Fragment(R.layout.fragment_variable) {

    private var _binding: FragmentVariableBinding? = null
    private val binding get() = _binding!!

    private val adapter = VariableAdapter(
        this::onVariableEvent,
        this::getVariableSettings,
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentVariableBinding.bind(view)

        binding.recycler.adapter = adapter

        modifiers
            .onEach { values ->
                adapter.submitList(values.values.toList())
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        _binding = null

        super.onDestroyView()
    }

    private fun getVariableSettings(variableName: String): VariableSettings {
        return settings[variableName] ?: VariableSettings.DEFAULT
    }

    private fun onVariableEvent(event: VariableEvent) {
        when (event) {
            is VariableEvent.ValueChange -> {
                modifiers.value[event.variableName]?.let { updatedVariable ->
                    modifiers.value[updatedVariable.name] =
                        updatedVariable.copy(value = event.newValue)
                }
            }

            is VariableEvent.SettingChanged -> {
                val isEnabled = (event.setting as? VariableSetting.Enabled)?.isEnabled
                val autoincrement = event.setting as? VariableSetting.Autoincrement

                settings[event.variableName]?.let { variableSettings ->
                    settings[event.variableName] = variableSettings.copy(
                        isEnabled = isEnabled ?: variableSettings.isEnabled,
                        autoincrement = autoincrement ?: variableSettings.autoincrement,
                    )
                }
            }
        }
    }
}

public fun Boolean.toDebugVariable(
    name: String,
): Boolean {
    return this.toDebugVariableInternal(name)
}

public fun String.toDebugVariable(
    name: String,
): String {
    return this.toDebugVariableInternal(name)
}

public fun Number.toDebugVariable(
    name: String,
): Number {
    return this.toDebugVariableInternal(name)
}

private fun <T> T.toDebugVariableInternal(
    name: String,
): T {
    require(this is Number || this is String || this is Boolean)

    if (this is Boolean && name in settings.keys) return settings[name]?.isEnabled as? T ?: this

    val variableSettings = settings[name] ?: VariableSettings.DEFAULT

    if (!variableSettings.isEnabled) return this

    val savedValue = modifiers.value.getOrPut(name) {
        settings[name] = if (this is Boolean) {
            VariableSettings.DEFAULT.copy(isEnabled = this)
        } else {
            VariableSettings.DEFAULT
        }

        VariableItem(name, this.toString(), this!!::class.simpleName!!)
    }.value.let { savedValue ->
        val incrementStep = if (variableSettings.autoincrement.isEnabled) {
            variableSettings.autoincrement.step
        } else {
            .0
        }

        when (this) {
            is Int -> savedValue.toIntOrNull()?.plus(incrementStep)
            is Long -> savedValue.toLongOrNull()?.plus(incrementStep)
            is Short -> savedValue.toShortOrNull()?.plus(incrementStep)
            is Float -> savedValue.toFloatOrNull()?.plus(incrementStep)
            is Double -> savedValue.toDoubleOrNull()?.plus(incrementStep)
            is Boolean -> settings[name]?.isEnabled ?: this
            else -> savedValue
        } as? T
    }

    modifiers.tryEmit(modifiers.value)

    return savedValue ?: this
}
