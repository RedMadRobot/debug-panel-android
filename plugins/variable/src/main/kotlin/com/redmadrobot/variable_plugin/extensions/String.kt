package com.redmadrobot.variable_plugin.extensions

import kotlin.reflect.KClass

internal fun <T : Number> String.toNumberOrNull(kClass: KClass<T>): T? {
    return when (kClass) {
        Int::class -> this.toInt()
        UInt::class -> this.toUInt()
        Long::class -> this.toLong()
        ULong::class -> this.toULong()
        Short::class -> this.toShort()
        Float::class -> this.toFloat()
        Double::class -> this.toDouble()
        else -> null
    } as? T
}

internal fun <T : Number> String.toNumberSafe(kClass: KClass<T>): T {
    return if (this.any(Char::isDigit)) {
        this.toNumberOrNull(kClass)
    } else {
        0
    } as T
}
