package ru.mixail_akulov.a25_mvvm_foundation.views

/**
 * Общие действия, которые можно выполнять в модели представления
 * Эти действия не зависят от активити и нам не надо вызывать [whenActivityAction], нужен лишь
 * application-context, а он всегда есть во view-model.
 */
interface UiActions {

    /**
     * Показать простое всплывающее сообщение.
     */
    fun toast(message: String)

    /**
     * Получить содержимое строкового ресурса по его идентификатору.
     */
    fun getString(messageRes: Int, vararg args: Any): String

}