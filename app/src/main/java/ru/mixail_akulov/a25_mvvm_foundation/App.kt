package ru.mixail_akulov.a25_mvvm_foundation

import android.app.Application
import ru.mixail_akulov.a25_mvvm_foundation.model.colors.InMemoryColorsRepository

/**
 * Точка входа нашего приложения
 * Здесь мы храним экземпляры классов слоя модели.
 */
class App : Application() {

    /**
     * Размещайте здесь свои репозитории, сейчас у нас всего 1 репозиторий
     */
    val models = listOf<Any>(
        InMemoryColorsRepository() // через запятую указываются реализации репозиториев
    )

}