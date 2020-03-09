package com.redmadrobot.debug_panel.data

import io.reactivex.Single

interface DebugDataLoadingStrategy<T> {
    fun loadData(): Single<List<T>>
}
