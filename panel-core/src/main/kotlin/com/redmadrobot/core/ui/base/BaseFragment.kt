package com.redmadrobot.core.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import com.redmadrobot.core.extension.autoDispose
import com.redmadrobot.panel_core.R
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseFragment(private val layoutId: Int) : Fragment() {
    private val compositeDisposable by lazy { CompositeDisposable() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val contextThemeWrapper = ContextThemeWrapper(activity, R.style.DebugPanelTheme)
        val localInflater = layoutInflater.cloneInContext(contextThemeWrapper)
        return localInflater.inflate(layoutId, container, false)
    }

    override fun onDestroyView() {
        compositeDisposable.dispose()
        super.onDestroyView()
    }

    protected fun Disposable.autoDispose(): Disposable {
        this.autoDispose(compositeDisposable)
        return this
    }
}
