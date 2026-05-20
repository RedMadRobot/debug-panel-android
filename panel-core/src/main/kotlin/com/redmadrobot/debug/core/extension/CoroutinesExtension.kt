package com.redmadrobot.debug.core.extension

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Launches a coroutine with error handling.
 *
 * Catches all exceptions except [CancellationException], logs them via Timber.
 *
 * @param block suspend block to execute
 * @param onError callback invoked when an error occurs
 */
@Suppress("TooGenericExceptionCaught")
public fun CoroutineScope.safeLaunch(
    block: suspend CoroutineScope.() -> Unit,
    onError: (Throwable) -> Unit
) {
    launch {
        try {
            block.invoke(this)
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            Timber.e(exception)
            onError.invoke(exception)
        }
    }
}

/**
 * Launches a coroutine with error logging.
 *
 * Equivalent to [safeLaunch] with an empty error handler --
 * exceptions are only logged via Timber.
 *
 * @param block suspend block to execute
 */
public fun CoroutineScope.safeLaunch(
    block: suspend CoroutineScope.() -> Unit
) {
    safeLaunch(block, { })
}
