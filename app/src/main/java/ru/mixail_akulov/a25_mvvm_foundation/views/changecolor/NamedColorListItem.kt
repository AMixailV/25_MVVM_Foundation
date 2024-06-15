package ru.mixail_akulov.a25_mvvm_foundation.views.changecolor

import ru.mixail_akulov.a25_mvvm_foundation.model.colors.NamedColor

/**
 * Представляет элемент списка для цвета; его можно выбрать или нет
 */
data class NamedColorListItem(
    val namedColor: NamedColor,
    val selected: Boolean
)