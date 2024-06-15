package ru.mixail_akulov.a25_mvvm_foundation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import ru.mixail_akulov.a25_mvvm_foundation.views.HasScreenTitle
import ru.mixail_akulov.a25_mvvm_foundation.views.base.BaseFragment
import ru.mixail_akulov.a25_mvvm_foundation.views.currentcolor.CurrentColorFragment

/**
 * Это приложение представляет собой приложение с одним действием.
 * MainActivity — это контейнер для всех экранов.
 */
class MainActivity : AppCompatActivity() {

    private val activityViewModel by viewModels<MainViewModel> { AndroidViewModelFactory(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            // определить начальный экран, который должен запускаться при запуске приложения.
            activityViewModel.launchFragment(
                activity = this,
                screen = CurrentColorFragment.Screen(),
                addToBackStack = false
            )
        }

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentCallbacks, false)
    }

    override fun onDestroy() {
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentCallbacks)
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onResume() {
        super.onResume()
        // выполнять действия навигации только тогда, когда активность активна
        activityViewModel.whenActivityActive.resource = this
    }

    override fun onPause() {
        super.onPause()
        // отложить навигационные действия, если активность не активна
        activityViewModel.whenActivityActive.resource = null
    }

    // верхний toolBar
    fun notifyScreenUpdates() {
        val f = supportFragmentManager.findFragmentById(R.id.fragmentContainer)

        // нажатие возврата на предыдущий экран
        if (supportFragmentManager.backStackEntryCount > 0) {
            // более 1 экрана -> показать кнопку «Назад» на панели инструментов
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        } else {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }

        //отображение заголовка экрана
        if (f is HasScreenTitle && f.getScreenTitle() != null) { // если фрагмент реализует HasScreenTitle и ...
            // фрагмент имеет собственный заголовок экрана -> отображать его
            supportActionBar?.title = f.getScreenTitle()
        } else {
            supportActionBar?.title = getString(R.string.app_name)
        }

        val result = activityViewModel.result.value?.getValue() ?: return
        if (f is BaseFragment) {
            // если имеет результат, который может быть доставлен в модель представления экрана
            f.viewModel.onResult(result)
        }
    }

    private val fragmentCallbacks = object : FragmentManager.FragmentLifecycleCallbacks() {
        // какой-то экран (фрагмент), лежащий в backStack, стал активным
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            notifyScreenUpdates() // прикручиваем к нему наш toolBar
        }
    }

}