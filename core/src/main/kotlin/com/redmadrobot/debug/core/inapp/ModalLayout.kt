package com.redmadrobot.debug.core.inapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.redmadrobot.panel_core.R
import com.redmadrobot.debug_panel_common.R as CommonR

internal class ModalLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CoordinatorLayout(context, attrs, defStyleAttr) {

    /**
     * Paint для отрисовки "ручки" модального окна
     */
    private val handlePaint = Paint().apply {
        color = ContextCompat.getColor(context, CommonR.color.super_light_gray)
        style = Paint.Style.FILL
    }

    /**
     * Координаты для отрисовки "ручки" модального окна
     */
    private val handleRect by lazy {
        val height = resources.getDimensionPixelSize(R.dimen.debug_panel_handle_height).toFloat()
        val width = resources.getDimensionPixelSize(R.dimen.debug_panel_handle_width).toFloat()
        val displayWidthCenter = resources.displayMetrics.widthPixels / 2
        val topMargin = resources.getDimensionPixelSize(R.dimen.debug_panel_handle_top_margin).toFloat()

        RectF(
            (displayWidthCenter - width / 2),
            topMargin,
            (displayWidthCenter + width / 2),
            height + topMargin
        )
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (parent is CoordinatorLayout) {
            background = ContextCompat.getDrawable(context, R.drawable.shape_scrollable_sheet)
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        val radius = resources.getDimensionPixelSize(R.dimen.debug_panel_handle_height) / 2
        canvas.drawRoundRect(
            handleRect,
            radius.toFloat(),
            radius.toFloat(),
            handlePaint
        )
        super.dispatchDraw(canvas)
    }
}
