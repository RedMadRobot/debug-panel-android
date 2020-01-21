package com.redmadrobot.debug_panel.ui.base

import androidx.fragment.app.Fragment
import com.redmadrobot.debug_panel.extension.autoDispose
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseFragment(layoutId: Int) : Fragment(layoutId) {
    protected val compositeDisposable by lazy { CompositeDisposable() }

    override fun onDestroyView() {
        compositeDisposable.dispose()
        super.onDestroyView()
    }

    fun Disposable.autoDispose(): Disposable {
        this.autoDispose(compositeDisposable)
        return this
    }
}
