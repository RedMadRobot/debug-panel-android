package com.redmadrobot.flipper_plugin.ui.recycler

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.redmadrobot.flipper.config.FlipperValue
import com.redmadrobot.flipper_plugin.databinding.ItemFlipperFeatureStringBinding

internal class StringFeatureViewHolder(
    itemView: View,
    private val onFeatureValueChanged: (feature: String, value: FlipperValue) -> Unit,
) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemFlipperFeatureStringBinding.bind(itemView)

    fun bind(
        featureId: String,
        value: FlipperValue.StringValue,
        description: String,
        editable: Boolean,
    ) = with(binding) {

        flipperPluginValue.apply {
            setText(value.value)
            isEnabled = editable
            clearFocus()
        }
        flipperPluginValueLabel.text = description

        flipperPluginUpdate.setOnClickListener {
            onFeatureValueChanged(featureId, FlipperValue.StringValue(flipperPluginValue.text.toString()))
            flipperPluginValue.clearFocus()
        }

        flipperPluginValue.setOnFocusChangeListener { _, hasFocus ->
            flipperPluginUpdate.isVisible = hasFocus
        }
    }
}
