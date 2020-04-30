package com.redmadrobot.debug_panel.ui.view

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class ItemTouchHelperCallback(
    private val onSwiped: (Int) -> Unit,
    private val canBeSwiped: ((Int) -> Boolean)? = null
) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val swipeFlags = if (canBeSwiped?.invoke(viewHolder.layoutPosition) == true) {
            ItemTouchHelper.START or ItemTouchHelper.END
        } else {
            0
        }
        return makeMovementFlags(0, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onSwiped.invoke(viewHolder.adapterPosition)
    }
}
