package com.redmadrobot.flipper_plugin.ui.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.redmadrobot.flipper.Feature
import com.redmadrobot.flipper.config.FlipperValue
import com.redmadrobot.flipper_plugin.databinding.ItemFlipperFeatureBooleanBinding
import com.redmadrobot.flipper_plugin.ui.item.FlipperFeatureItem

internal class FlipperFeaturesAdapter(
    private val onFeatureValueChanged: (feature: Feature, value: FlipperValue) -> Unit,
) : ListAdapter<FlipperFeatureItem, RecyclerView.ViewHolder>(
    DIFF_CALLBACK,
) {

    internal companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FlipperFeatureItem>() {
            override fun areItemsTheSame(
                oldItem: FlipperFeatureItem,
                newItem: FlipperFeatureItem,
            ): Boolean {
                return oldItem.feature.id == newItem.feature.id
            }

            override fun areContentsTheSame(
                oldItem: FlipperFeatureItem,
                newItem: FlipperFeatureItem,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).value::class.hashCode()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            FlipperValue.BooleanValue::class.hashCode() -> {
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

            else -> throw IllegalStateException("Can't create viewHolder for given viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (item.value) {
            is FlipperValue.BooleanValue -> {
                (holder as BooleanFeatureViewHolder).bind(item.feature, item.value)
            }

            else -> {
                throw IllegalStateException(
                    "Can't bind item with value: ${item.value::class.simpleName}"
                )
            }
        }
    }
}
