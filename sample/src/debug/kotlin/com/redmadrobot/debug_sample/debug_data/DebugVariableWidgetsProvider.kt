package com.redmadrobot.debug_sample.debug_data

import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.RequiresApi
import com.redmadrobot.debug_panel_core.data.DebugDataProvider
import com.redmadrobot.debugpanel.R
import com.redmadrobot.variable_plugin.plugin.*
import java.time.LocalDateTime
import java.time.Year
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

internal class DebugVariableWidgetsProvider : DebugDataProvider<List<VariableWidget<Any>>> {

    @Suppress("UNCHECKED_CAST")
    override fun provideData(): List<VariableWidget<Any>> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            listOf(
                getTestWidget()
            ) as List<VariableWidget<Any>>
        } else {
            emptyList()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTestWidget(): VariableWidget<LocalDateTime> {
        return object : VariableWidget<LocalDateTime>(LocalDateTime::class) {

            override fun createViewHolder(
                parent: ViewGroup
            ): VariableWidgetViewHolder<LocalDateTime> {
                val view = LayoutInflater
                    .from(parent.context)
                    .inflate(
                        R.layout.item_variable_widget_localdatetime,
                        parent,
                        false
                    )

                return getTestWidgetViewHolder(view)
            }

            override fun getSupportedSettings(): VariableWidgetSettings<LocalDateTime>? {
                return getTestSetting(Year.now().value)
            }

            override fun createSettingsViewHolder(
                parent: ViewGroup
            ): VariableWidgetSettingsViewHolder<LocalDateTime, VariableWidgetSettings<LocalDateTime>>? {
                val view = LayoutInflater
                    .from(parent.context)
                    .inflate(
                        R.layout.item_variable_widget_localdatetime_settings,
                        parent,
                        false
                    )

                return getTestSettingsViewHolder(view)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTestWidgetViewHolder(
        view: View,
    ): VariableWidgetViewHolder<LocalDateTime> {
        return object : VariableWidgetViewHolder<LocalDateTime>(view) {

            private val dateFormat = DateTimeFormatter.ISO_LOCAL_DATE_TIME

            private val editText =
                itemView.findViewById<EditText>(R.id.item_variable_widget_date_time)

            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) = Unit

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) = Unit

                override fun afterTextChanged(text: Editable?) {
                    val newDate = try {
                        LocalDateTime.parse(
                            text?.toString().orEmpty(),
                            dateFormat
                        )
                    } catch (e: DateTimeParseException) {
                        null
                    }

                    newDate?.let(onValueChangedAction::invoke)
                }
            }

            private var onValueChangedAction: (LocalDateTime) -> Unit = {}

            override fun bind(
                item: VariableItem<LocalDateTime>,
                onValueChanged: (LocalDateTime) -> Unit
            ) = with(editText) {
                removeTextChangedListener(textWatcher)

                onValueChangedAction = onValueChanged

                setText(item.value.format(dateFormat))

                addTextChangedListener(textWatcher)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTestSetting(year: Int): VariableWidgetSettings<LocalDateTime> {
        return object : VariableWidgetSettings<LocalDateTime>() {
            override fun apply(item: VariableItem<LocalDateTime>): VariableItem<LocalDateTime>? {
                return item.copy(
                    value = item.value.withYear(year)
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTestSettingsViewHolder(
        view: View
    ): VariableWidgetSettingsViewHolder<LocalDateTime, VariableWidgetSettings<LocalDateTime>> {
        return object :
            VariableWidgetSettingsViewHolder<LocalDateTime, VariableWidgetSettings<LocalDateTime>>(view) {

            private val editTextYear = itemView.findViewById<EditText>(
                R.id.item_variable_widget_localdatetime_settings_year
            )

            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) = Unit

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) = Unit

                override fun afterTextChanged(text: Editable?) {
                    val newDate = try {
                        getTestSetting(Year.parse(text?.toString().orEmpty()).value)
                    } catch (e: DateTimeParseException) {
                        null
                    }

                    newDate?.let(onSettingsChangedAction::invoke)
                }
            }

            private var onSettingsChangedAction: (VariableWidgetSettings<LocalDateTime>) -> Unit = {}

            override fun bind(
                settings: VariableWidgetSettings<LocalDateTime>,
                onSettingsChanged: (VariableWidgetSettings<LocalDateTime>) -> Unit
            ) {
                editTextYear.removeTextChangedListener(textWatcher)

                onSettingsChangedAction = onSettingsChanged

                editTextYear.addTextChangedListener(textWatcher)
            }
        }
    }
}
