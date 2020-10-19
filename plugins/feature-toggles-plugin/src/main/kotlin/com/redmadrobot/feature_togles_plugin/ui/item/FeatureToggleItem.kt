package com.redmadrobot.feature_togles_plugin.ui.item

import com.redmadrobot.feature_toggles_plugin.R
import com.redmadrobot.feature_togles_plugin.data.model.FeatureToggle
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_feature_toggle.view.*

class FeatureToggleItem(
    private val featureToggle: FeatureToggle,
    private val checkedChangeListener: (FeatureToggle, isChecked: Boolean) -> Unit,
    private val isChangeEnabled: Boolean
) : Item() {

    override fun getLayout() = R.layout.item_feature_toggle

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        with(viewHolder.itemView) {
            item_feature_toggle_switch.isEnabled = isChangeEnabled
            item_feature_toggle_name.text = featureToggle.name
            item_feature_toggle_switch.setOnCheckedChangeListener { _, isChecked ->
                if (isChangeEnabled) {
                    checkedChangeListener.invoke(featureToggle, isChecked)
                }
            }
            item_feature_toggle_switch.isChecked = featureToggle.value
        }
    }
}
