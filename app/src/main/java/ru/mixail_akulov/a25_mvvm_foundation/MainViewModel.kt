package ru.mixail_akulov.a25_mvvm_foundation

import android.app.Application
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import ru.mixail_akulov.a25_mvvm_foundation.model.utils.Event
import ru.mixail_akulov.a25_mvvm_foundation.model.utils.ResourceActions
import ru.mixail_akulov.a25_mvvm_foundation.views.Navigator
import ru.mixail_akulov.a25_mvvm_foundation.views.base.BaseScreen
import ru.mixail_akulov.a25_mvvm_foundation.views.base.LiveEvent
import ru.mixail_akulov.a25_mvvm_foundation.views.base.MutableLiveEvent
import ru.mixail_akulov.a25_mvvm_foundation.views.UiActions

const val ARG_SCREEN = "ARG_SCREEN"

/**
 * Реализация [Navigator] и [UiActions].
 * Он основан на activity view-model, поскольку экземпляры [Navigator] и [UiActions]
 * должны быть доступны из view-models фрагментов (обычно они передаются конструктору view-model).
 *
 * Эта view-model расширяет [AndroidViewModel], что означает, что это не «обычная» view-model,
 * и она может содержать зависимости от Android (контекст, пакеты и т. д.).
 */
class MainViewModel(
    application: Application
) : AndroidViewModel(application), Navigator, UiActions {

    // Очередь действий, где действия выполняются только при наличии ресурса, назначается в методе o
    // nResume() в MainActivity.
    // Если это не так, действие добавляется в очередь и ожидает, пока ресурс не станет доступным.
    // Чтобы не начать выполнение действия без живой активити или фрагмента, т.к. ЖЦикл view-model больше
    val whenActivityActive = ResourceActions<MainActivity>()

    // Создаем слушателей для результата
    private val _result = MutableLiveEvent<Any>()
    val result: LiveEvent<Any> = _result

    // логика выполнения действия определена внутри [ResourceAction.invoke]
    override fun launch(screen: BaseScreen) = whenActivityActive {
        launchFragment(it, screen)
    }

    // логика выполнения действия определена внутри [ResourceAction.invoke]
    override fun goBack(result: Any?) = whenActivityActive {
        if (result != null) {
            _result.value = Event(result)
        }
        it.onBackPressed()
    }

    override fun toast(message: String) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show()
    }

    override fun getString(messageRes: Int, vararg args: Any): String {
        return getApplication<App>().getString(messageRes, *args)
    }

    fun launchFragment(activity: MainActivity, screen: BaseScreen, addToBackStack: Boolean = true) {
        // поскольку классы экрана находятся внутри фрагментов -> мы можем создать фрагмент прямо с экрана
        val fragment = screen.javaClass.enclosingClass.newInstance() as Fragment
        // установить экранный объект в качестве аргумента фрагмента
        fragment.arguments = bundleOf(ARG_SCREEN to screen)

        val transaction = activity.supportFragmentManager.beginTransaction()
        if (addToBackStack) transaction.addToBackStack(null)
        transaction
            .setCustomAnimations(
                R.anim.enter,
                R.anim.exit,
                R.anim.pop_enter,
                R.anim.pop_exit
            )
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    override fun onCleared() {
        super.onCleared()
        whenActivityActive.clear()
    }
}