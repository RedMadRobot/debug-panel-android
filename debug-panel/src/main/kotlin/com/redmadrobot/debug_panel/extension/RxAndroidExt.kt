package com.redmadrobot.debug_panel.extension

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

//region Single
internal fun <T> Single<T>.subscribeOnIo(): Single<T> = subscribeOn(Schedulers.io())

internal fun <T> Single<T>.observeOnMain(): Single<T> = observeOn(AndroidSchedulers.mainThread())
//endregion

//region Completable
internal fun Completable.subscribeOnIo(): Completable = subscribeOn(Schedulers.io())

internal fun Completable.observeOnMain(): Completable = observeOn(AndroidSchedulers.mainThread())
//endregion
