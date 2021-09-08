package com.redmadrobot.flipper_plugin.ui.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.redmadrobot.flipper.Feature
import com.redmadrobot.flipper.config.FlipperValue
import com.redmadrobot.flipper_plugin.databinding.ItemFlipperFeatureBooleanBinding

internal class BooleanFeatureViewHolder(
    itemView: View,
    private val onFeatureValueChanged: (feature: Feature, value: FlipperValue) -> Unit,
) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemFlipperFeatureBooleanBinding.bind(itemView)

    fun bind(feature: Feature, value: FlipperValue.BooleanValue) {
        binding.checkbox.apply {
            setOnCheckedChangeListener(null)

            text = feature.id
            isChecked = value.value

            setOnCheckedChangeListener { _, isChecked ->
                onFeatureValueChanged.invoke(feature, FlipperValue.BooleanValue(isChecked))
            }
        }
    }
}
