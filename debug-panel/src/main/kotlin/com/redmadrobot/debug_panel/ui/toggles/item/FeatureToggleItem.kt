package com.redmadrobot.debug_panel.ui.toggles.item

import com.redmadrobot.debug_panel.R
import com.redmadrobot.debug_panel.inapp.toggles.FeatureToggle
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_feature_toggle.view.*

class FeatureToggleItem(
    private val featureToggle: FeatureToggle,
    private val checkedChangeListener: (FeatureToggle, isChecked: Boolean) -> Unit
) : Item() {

    override fun getLayout() = R.layout.item_feature_toggle

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        with(viewHolder.itemView) {
            item_feature_toggle_name.text = featureToggle.name
            item_feature_toggle_switch.isChecked = featureToggle.value
            item_feature_toggle_switch.setOnCheckedChangeListener { _, isChecked ->
                checkedChangeListener.invoke(featureToggle, isChecked)
            }
        }
    }
}
