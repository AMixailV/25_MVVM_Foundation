package ru.mixail_akulov.a25_mvvm_foundation.views.currentcolor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.mixail_akulov.a25_mvvm_foundation.R
import ru.mixail_akulov.a25_mvvm_foundation.model.colors.ColorListener
import ru.mixail_akulov.a25_mvvm_foundation.model.colors.ColorsRepository
import ru.mixail_akulov.a25_mvvm_foundation.model.colors.NamedColor
import ru.mixail_akulov.a25_mvvm_foundation.views.Navigator
import ru.mixail_akulov.a25_mvvm_foundation.views.base.BaseViewModel
import ru.mixail_akulov.a25_mvvm_foundation.views.changecolor.ChangeColorFragment
import ru.mixail_akulov.a25_mvvm_foundation.views.UiActions

class CurrentColorViewModel(
    private val navigator: Navigator,
    private val uiActions: UiActions,
    private val colorsRepository: ColorsRepository
) : BaseViewModel() {

    private val _currentColor = MutableLiveData<NamedColor>()
    val currentColor: LiveData<NamedColor> = _currentColor

    private val colorListener: ColorListener = {
        _currentColor.postValue(it)
    }

    // --- пример результатов прослушивания через модельный слой

    init {
        colorsRepository.addListener(colorListener)
    }

    override fun onCleared() {
        super.onCleared()
        colorsRepository.removeListener(colorListener)
    }

    // --- пример прослушивания результатов прямо с экрана
    // если модель хочет слушать результаты с экрана, она переопределяет этот метод

    override fun onResult(result: Any) {
        super.onResult(result)
        if (result is NamedColor) {
            val message = uiActions.getString(R.string.changed_color, result.name)
            uiActions.toast(message)
        }
    }

    // ---

    fun changeColor() {
        val currentColor = currentColor.value ?: return
        val screen = ChangeColorFragment.Screen(currentColor.id)
        navigator.launch(screen)
    }

}