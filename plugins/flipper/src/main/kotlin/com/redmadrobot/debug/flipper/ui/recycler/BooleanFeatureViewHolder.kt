package com.redmadrobot.debug.flipper.ui.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.redmadrobot.debug.flipper.databinding.ItemFlipperFeatureBooleanBinding
import com.redmadrobot.flipper.config.FlipperValue

internal class BooleanFeatureViewHolder(
    itemView: View,
    private val onFeatureValueChanged: (feature: String, value: FlipperValue) -> Unit,
) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemFlipperFeatureBooleanBinding.bind(itemView)

    fun bind(
        featureId: String,
        value: FlipperValue.BooleanValue,
        description: String,
        editable: Boolean,
    ) {
        binding.checkbox.apply {
            setOnCheckedChangeListener(null)

            text = description
            isEnabled = editable
            isChecked = value.value

            setOnCheckedChangeListener { _, isChecked ->
                onFeatureValueChanged.invoke(featureId, FlipperValue.BooleanValue(isChecked))
            }
        }
    }
}
