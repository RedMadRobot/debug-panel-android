package com.redmadrobot.debug_panel.ui.debugpanel

import androidx.annotation.StringRes
import com.redmadrobot.debug_panel.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_debug_feature.view.*

class DebugFeatureItem(
    @StringRes private val featureNameId: Int,
    private val onClicked: (Int) -> Unit
) : Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) = with(viewHolder) {
        containerView.item_debug_feature_label.text = containerView.context.getText(featureNameId)

        containerView.setOnClickListener {
            onClicked.invoke(featureNameId)
        }
    }

    override fun getLayout() = R.layout.item_debug_feature
}
