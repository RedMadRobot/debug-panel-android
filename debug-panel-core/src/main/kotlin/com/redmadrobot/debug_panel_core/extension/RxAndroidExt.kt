package com.redmadrobot.debug_panel_core.extension

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

//region Single
fun <T> Single<T>.subscribeOnIo(): Single<T> = subscribeOn(Schedulers.io())

fun <T> Single<T>.observeOnMain(): Single<T> = observeOn(AndroidSchedulers.mainThread())

fun <T> Single<List<T>>.zipList(anotherSource: Single<List<T>>): Single<List<T>> {
    return this.zipWith(
        anotherSource,
        BiFunction<List<T>, List<T>, List<T>> { currentList, anotherList ->
            currentList + anotherList
        }
    )
}
//endregion

//region Maybe
fun <T> Maybe<T>.observeOnMain(): Maybe<T> = observeOn(AndroidSchedulers.mainThread())
//endregion

//region Completable
fun Completable.subscribeOnIo(): Completable = subscribeOn(Schedulers.io())

fun Completable.observeOnMain(): Completable = observeOn(AndroidSchedulers.mainThread())
//endregion


//
fun Disposable.autoDispose(compositeDisposable: CompositeDisposable): Disposable {
    compositeDisposable.add(this)
    return this
}
//endregion
