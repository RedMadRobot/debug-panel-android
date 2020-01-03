package com.redmadrobot.debug_panel.extension

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers

//region Single

internal fun <T> Single<T>.observeOnMain(): Single<T> = observeOn(AndroidSchedulers.mainThread())
//endregion
