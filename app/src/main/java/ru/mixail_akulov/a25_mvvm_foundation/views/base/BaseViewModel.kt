package ru.mixail_akulov.a25_mvvm_foundation.views.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.mixail_akulov.a25_mvvm_foundation.model.utils.Event

// Чтобы сократить запись создаем новый тип, который короче пишется
typealias LiveEvent<T> = LiveData<Event<T>>
typealias MutableLiveEvent<T> = MutableLiveData<Event<T>>

/**
 * Base class for all view-models.
 */
open class BaseViewModel : ViewModel() {

    /**
     * Override this method in child classes if you want to listen for results
     * from other screens
     */
    open fun onResult(result: Any) {

    }
}