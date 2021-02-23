package com.redmadrobot.debug_panel_common.extension

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Запуск корутины с встроенной обработкой ошибки
 * */
public fun CoroutineScope.safeLaunch(
    block: suspend CoroutineScope.() -> Unit,
    onError: (Throwable) -> Unit
) {
    launch {
        try {
            block.invoke(this)
        } catch (exception: Exception) {
            if (exception !is CancellationException) {
                Timber.e(exception)
                onError.invoke(exception)
            }
        }
    }
}

/**
 * Запуск корутины с встроенным логированием ошибки
 * */
public fun CoroutineScope.safeLaunch(
    block: suspend CoroutineScope.() -> Unit
) {
    safeLaunch(block, { })
}
