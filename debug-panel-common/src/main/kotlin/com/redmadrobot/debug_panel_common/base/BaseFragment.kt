package com.redmadrobot.debug_panel_common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import com.redmadrobot.debug_panel_common.R

public open class BaseFragment(private val layoutId: Int) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val contextThemeWrapper = ContextThemeWrapper(activity, R.style.DebugPanelTheme)
        val localInflater = layoutInflater.cloneInContext(contextThemeWrapper)
        return localInflater.inflate(layoutId, container, false)
    }

}
