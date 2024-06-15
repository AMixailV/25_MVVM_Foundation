package ru.mixail_akulov.a25_mvvm_foundation.model.colors

import ru.mixail_akulov.a25_mvvm_foundation.model.Repository

typealias ColorListener = (NamedColor) -> Unit

/**
 * Repository interface example.
 *
 * Предоставляет доступ к доступным цветам и текущему выбранному цвету.
 */
interface ColorsRepository : Repository {

    var currentColor: NamedColor

    /**
     * Получить список всех доступных цветов, которые может выбрать пользователь.
     */
    fun getAvailableColors(): List<NamedColor>

    /**
     * Get the color content by its ID
     */
    fun getById(id: Long): NamedColor

    /**
     * Listen for the current color changes.
     * Слушатель запускается немедленно с текущим значением при вызове этого метода.
     */
    fun addListener(listener: ColorListener)

    /**
     * Stop listening for the current color changes
     */
    fun removeListener(listener: ColorListener)

}