package ru.mixail_akulov.a25_mvvm_foundation.views.base

import androidx.fragment.app.Fragment
import ru.mixail_akulov.a25_mvvm_foundation.MainActivity

/**
 * Base class for all fragments
 */
abstract class BaseFragment : Fragment() {

    /**
     * View-model that manages this fragment
     */
    abstract val viewModel: BaseViewModel

    /**
     * Вызовите этот метод, когда элементы управления действиями (например, панель инструментов)
     * должны быть повторно отображены.
     */
    fun notifyScreenUpdates() {
        // если у вас более 1 активности -> вы должны использовать отдельный интерфейс
        // вместо прямого приведения к MainActivity
        (requireActivity() as MainActivity).notifyScreenUpdates()
    }
}