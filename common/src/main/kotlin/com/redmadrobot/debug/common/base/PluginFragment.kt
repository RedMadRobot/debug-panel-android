package com.redmadrobot.debug.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import com.redmadrobot.debug.panel.common.R

public open class PluginFragment(private val layoutId: Int) : Fragment() {

    protected val isSettingMode: Boolean by lazy {
        activity?.javaClass?.simpleName == "DebugActivity"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val contextThemeWrapper = ContextThemeWrapper(activity, R.style.DebugPanelTheme)
        val localInflater = inflater.cloneInContext(contextThemeWrapper)
        return localInflater.inflate(layoutId, container, false)
    }
}
