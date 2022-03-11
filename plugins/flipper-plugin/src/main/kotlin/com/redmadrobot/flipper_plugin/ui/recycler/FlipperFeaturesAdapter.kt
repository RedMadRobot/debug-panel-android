package com.redmadrobot.flipper_plugin.ui.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.redmadrobot.flipper.config.FlipperValue
import com.redmadrobot.flipper_plugin.databinding.ItemFlipperFeatureBooleanBinding
import com.redmadrobot.flipper_plugin.databinding.ItemFlipperFeatureGroupBinding
import com.redmadrobot.flipper_plugin.ui.data.FlipperFeature
import com.redmadrobot.flipper_plugin.ui.data.FlipperFeature.Group
import com.redmadrobot.flipper_plugin.ui.data.FlipperFeature.Item

internal class FlipperFeaturesAdapter(
    private val onFeatureValueChanged: (featureId: String, value: FlipperValue) -> Unit,
    private val onGroupToggleStateChanged: (groupName: String, checked: Boolean) -> Unit,
) : ListAdapter<FlipperFeature, RecyclerView.ViewHolder>(
    FlipperFeaturesDiffCallback(),
) {

    override fun getItemViewType(position: Int): Int {
        return when (val item = getItem(position)) {
            is Group -> ViewType.GROUP.ordinal

            is Item -> {
                when (item.value) {
                    is FlipperValue.BooleanValue -> ViewType.BOOLEAN.ordinal
                    else -> error("FlipperValue ${item.value::class} not supported")
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (ViewType.values()[viewType]) {
            ViewType.BOOLEAN -> {
                val featureBinding = ItemFlipperFeatureBooleanBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

                BooleanFeatureViewHolder(
                    itemView = featureBinding.root,
                    onFeatureValueChanged = onFeatureValueChanged::invoke,
                )
            }

            ViewType.GROUP -> {
                val binding = ItemFlipperFeatureGroupBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

                GroupViewHolder(
                    itemView = binding.root,
                    onTogglesStateChanged = onGroupToggleStateChanged::invoke,
                )
            }

            else -> throw IllegalStateException("Can't create viewHolder for given viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        when (ViewType.values()[getItemViewType(position)]) {
            ViewType.GROUP -> {
                val group = item as Group
                val groupHolder = holder as GroupViewHolder

                groupHolder.bind(group.name, group.allEnabled)
            }

            ViewType.BOOLEAN -> {
                val booleanItem = item as Item
                val booleanHolder = holder as BooleanFeatureViewHolder

                booleanHolder.bind(
                    featureId = booleanItem.id,
                    value = booleanItem.value as FlipperValue.BooleanValue,
                    description = booleanItem.description,
                )
            }
        }
    }

    private enum class ViewType {
        GROUP,
        BOOLEAN,
    }
}
