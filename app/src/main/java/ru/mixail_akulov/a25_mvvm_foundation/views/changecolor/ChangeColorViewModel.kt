package ru.mixail_akulov.a25_mvvm_foundation.views.changecolor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import ru.mixail_akulov.a25_mvvm_foundation.R
import ru.mixail_akulov.a25_mvvm_foundation.model.colors.ColorsRepository
import ru.mixail_akulov.a25_mvvm_foundation.model.colors.NamedColor
import ru.mixail_akulov.a25_mvvm_foundation.views.Navigator
import ru.mixail_akulov.a25_mvvm_foundation.views.UiActions
import ru.mixail_akulov.a25_mvvm_foundation.views.base.BaseViewModel
// в аргументах конструктора указываются только интерфейсы, чтобы не менять в дальнейшем саму вью-модель,
// а делать изменениятолько в модели
class ChangeColorViewModel(
    screen: ChangeColorFragment.Screen,
    private val navigator: Navigator,
    private val uiActions: UiActions,
    private val colorsRepository: ColorsRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(), ColorsAdapter.Listener {

    // источники ввода
    private val _availableColors = MutableLiveData<List<NamedColor>>()
    private val _currentColorId = savedStateHandle.getLiveData("currentColorId", screen.currentColorId)

    // Основное предназначение (содержит объединенные значения из _availableColors & _currentColorId)
    private val _colorsList = MediatorLiveData<List<NamedColorListItem>>()
    val colorsList: LiveData<List<NamedColorListItem>> = _colorsList

    // стороны назначения, также тот же результат может быть достигнут с помощью функции Transformations.map().
    private val _screenTitle = MutableLiveData<String>()
    val screenTitle: LiveData<String> = _screenTitle

    init {
        _availableColors.value = colorsRepository.getAvailableColors()
        // initializing MediatorLiveData
        _colorsList.addSource(_availableColors) { mergeSources() }
        _colorsList.addSource(_currentColorId) { mergeSources() }
    }

    override fun onColorChosen(namedColor: NamedColor) {
        _currentColorId.value = namedColor.id
    }

    fun onSavePressed() {
        val currentColorId = _currentColorId.value ?: return
        val currentColor = colorsRepository.getById(currentColorId)
        colorsRepository.currentColor = currentColor
        navigator.goBack(result = currentColor)
    }

    fun onCancelPressed() {
        navigator.goBack()
    }

    /**
     * [MediatorLiveData] может прослушивать другие экземпляры LiveData (даже более 1) и объединять их значения.
     * Здесь мы слушаем список доступных цветов ([_availableColors] живые данные) +
     * идентификатор текущего цвета ([_currentColorId] живые данные),
     * затем мы используем оба этих значения для создания списка [NamedColorListItem],
     * это список для отображения в RecyclerView.
     */
    private fun mergeSources() {
        val colors = _availableColors.value ?: return
        val currentColorId = _currentColorId.value ?: return
        val currentColor = colors.first { it.id == currentColorId }
        _colorsList.value = colors.map { NamedColorListItem(it, currentColorId == it.id) }
        _screenTitle.value = uiActions.getString(R.string.change_color_screen_title, currentColor.name)
    }

}