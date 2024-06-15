package ru.mixail_akulov.a25_mvvm_foundation.model.utils

import androidx.lifecycle.LiveData

/**
 * Represents "побочный эффект".
 * Used in [LiveData] как обертка для событий.
 */
class Event<T>(
    private val value: T
) {
    private var handled: Boolean = false

    fun getValue(): T? {
        if (handled) return null
        handled = true
        return value
    }

}