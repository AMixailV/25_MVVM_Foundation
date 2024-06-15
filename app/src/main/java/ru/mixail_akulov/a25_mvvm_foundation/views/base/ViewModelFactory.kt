package ru.mixail_akulov.a25_mvvm_foundation.views.base

import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.savedstate.SavedStateRegistryOwner
import ru.mixail_akulov.a25_mvvm_foundation.ARG_SCREEN
import ru.mixail_akulov.a25_mvvm_foundation.App
import ru.mixail_akulov.a25_mvvm_foundation.MainViewModel
import java.lang.reflect.Constructor

/**
 * Используйте этот метод для получения моделей представления из ваших фрагментов.
 */
inline fun <reified VM : ViewModel> BaseFragment.screenViewModel() = viewModels<VM> {
    val application = requireActivity().application as App
    val screen = requireArguments().getSerializable(ARG_SCREEN) as BaseScreen

    // использование Providers API напрямую для получения экземпляра MainViewModel
    val provider = ViewModelProvider(requireActivity(), AndroidViewModelFactory(application))
    val mainViewModel = provider[MainViewModel::class.java]

    // формирование списка доступных зависимостей:
    // - singleton зависимости области видимости (repositories) -> from App class
    // - activity VM зависимости области действия -> from MainViewModel
    // - screen VM зависимости области действия -> screen args
    val dependencies = listOf(screen, mainViewModel) + application.models

    // creating factory
    ViewModelFactory(dependencies, this)
}

// наследует AbstractSavedStateViewModelFactory, чтобы использовать во вью-модели SavedStateHandle, через
// create(), куда он придет параметром.
class ViewModelFactory(
    private val dependencies: List<Any>,
    owner: SavedStateRegistryOwner
) : AbstractSavedStateViewModelFactory(owner, null) {

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        val constructors = modelClass.constructors
        val constructor = constructors.maxByOrNull { it.typeParameters.size }!!

        // - SavedStateHandle также является зависимостью от screen VM scope,
        // но получить ее мы можем только здесь, поэтому объединяем ее со списком других зависимостей:
        val dependenciesWithSavedState = dependencies + handle

        // создание списка аргументов для передачи в view-model's constructor
        val arguments = findDependencies(constructor, dependenciesWithSavedState)

        // creating view-model
        return constructor.newInstance(*arguments.toTypedArray()) as T
    }

    private fun findDependencies(constructor: Constructor<*>, dependencies: List<Any>): List<Any> {
        val args = mutableListOf<Any>()
        // здесь мы перебираем аргументы конструктора модели представления и для каждого аргумента
        // ищем зависимость, которая может быть назначена аргументу
        constructor.parameterTypes.forEach { parameterClass ->
            val dependency = dependencies.first { parameterClass.isAssignableFrom(it.javaClass) }
            args.add(dependency)
        }
        return args
    }

}