package com.redmadrobot.debug_panel.base

import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseFragment(layoutId: Int) : Fragment(layoutId) {
    protected val compositeDisposable by lazy { CompositeDisposable() }

    override fun onDestroyView() {
        compositeDisposable.dispose()
        super.onDestroyView()
    }

    fun Disposable.autoDispose(): Disposable {
        compositeDisposable.add(this)
        return this
    }
}
