package com.redmadrobot.debug.plugin.flipper.ui.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.redmadrobot.debug.plugin.flipper.databinding.ItemFlipperFeatureBooleanBinding
import com.redmadrobot.debug.plugin.flipper.databinding.ItemFlipperFeatureGroupBinding
import com.redmadrobot.debug.plugin.flipper.databinding.ItemFlipperFeatureStringBinding
import com.redmadrobot.debug.plugin.flipper.ui.data.FlipperItem
import com.redmadrobot.debug.plugin.flipper.ui.data.FlipperItem.Feature
import com.redmadrobot.debug.plugin.flipper.ui.data.FlipperItem.Group
import com.redmadrobot.flipper.config.FlipperValue

internal class FlipperFeaturesAdapter(
    private val onGroupClick: (groupName: String) -> Unit,
    private val onFeatureValueChanged: (featureId: String, value: FlipperValue) -> Unit,
    private val onGroupToggleStateChanged: (groupName: String, checked: Boolean) -> Unit,
) : ListAdapter<FlipperItem, RecyclerView.ViewHolder>(
    FlipperFeaturesDiffCallback(),
) {

    override fun getItemViewType(position: Int): Int {
        return when (val item = getItem(position)) {
            is Group -> ViewType.GROUP.ordinal

            is Feature -> {
                when (item.value) {
                    is FlipperValue.BooleanValue -> ViewType.BOOLEAN.ordinal
                    is FlipperValue.StringValue -> ViewType.STRING.ordinal
                    else -> error("FlipperValue ${item.value::class} not supported")
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (ViewType.values()[viewType]) {
            ViewType.BOOLEAN -> {
                val binding = ItemFlipperFeatureBooleanBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

                BooleanFeatureViewHolder(
                    itemView = binding.root,
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
                    onGroupClick = onGroupClick::invoke,
                    onTogglesStateChanged = onGroupToggleStateChanged::invoke,
                )
            }

            ViewType.STRING -> {
                val binding = ItemFlipperFeatureStringBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

                StringFeatureViewHolder(
                    itemView = binding.root,
                    onFeatureValueChanged = onFeatureValueChanged::invoke,
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

                groupHolder.bind(group.name, group.editable, group.allEnabled)
            }

            ViewType.BOOLEAN -> {
                val booleanItem = item as Feature
                val booleanHolder = holder as BooleanFeatureViewHolder

                booleanHolder.bind(
                    featureId = booleanItem.id,
                    value = booleanItem.value as FlipperValue.BooleanValue,
                    editable = booleanItem.editable,
                    description = booleanItem.description,
                )
            }

            ViewType.STRING -> {
                val booleanItem = item as Feature
                val stringHolder = holder as StringFeatureViewHolder

                stringHolder.bind(
                    featureId = booleanItem.id,
                    value = booleanItem.value as FlipperValue.StringValue,
                    editable = booleanItem.editable,
                    description = booleanItem.description,
                )
            }
        }
    }

    private enum class ViewType {
        GROUP,
        BOOLEAN,
        STRING
    }
}
