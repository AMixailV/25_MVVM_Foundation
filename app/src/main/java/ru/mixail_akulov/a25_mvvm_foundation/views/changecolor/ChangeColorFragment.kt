package ru.mixail_akulov.a25_mvvm_foundation.views.changecolor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.lifecycle.SavedStateHandle
import androidx.recyclerview.widget.GridLayoutManager
import ru.mixail_akulov.a25_mvvm_foundation.R
import ru.mixail_akulov.a25_mvvm_foundation.databinding.FragmentChangeColorBinding
import ru.mixail_akulov.a25_mvvm_foundation.views.HasScreenTitle
import ru.mixail_akulov.a25_mvvm_foundation.views.base.BaseFragment
import ru.mixail_akulov.a25_mvvm_foundation.views.base.BaseScreen
import ru.mixail_akulov.a25_mvvm_foundation.views.base.screenViewModel

/**
 * Экран для изменения цвета.
 * 1) Отображает список доступных цветов
 * 2) Позволяет выбрать нужный цвет
 * 3) Выбранный цвет сохраняется только после нажатия кнопки "Сохранить"
 * 4) Текущий выбор сохраняется через [SavedStateHandle] (see [ChangeColorViewModel])
 */
class ChangeColorFragment : BaseFragment(), HasScreenTitle {

    /**
     * Этот экран имеет 1 аргумент: идентификатор цвета, который будет отображаться как выбранный.
     */
    class Screen(
        val currentColorId: Long // только serializable типы даных
    ) : BaseScreen

    override val viewModel by screenViewModel<ChangeColorViewModel>()

    /**
     * Пример динамического заголовка экрана
     */
    override fun getScreenTitle(): String? = viewModel.screenTitle.value

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentChangeColorBinding.inflate(inflater, container, false)

        val adapter = ColorsAdapter(viewModel)
        setupLayoutManager(binding, adapter)

        binding.saveButton.setOnClickListener { viewModel.onSavePressed() }
        binding.cancelButton.setOnClickListener { viewModel.onCancelPressed() }

        viewModel.colorsList.observe(viewLifecycleOwner) {
            adapter.items = it
        }
        viewModel.screenTitle.observe(viewLifecycleOwner) {
            // если заголовок экрана изменен -> необходимо уведомлять об обновлениях
            notifyScreenUpdates()
        }

        return binding.root
    }

    private fun setupLayoutManager(binding: FragmentChangeColorBinding, adapter: ColorsAdapter) {
        // waiting for list width
        binding.colorsRecyclerView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.colorsRecyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val width = binding.colorsRecyclerView.width
                val itemWidth = resources.getDimensionPixelSize(R.dimen.item_width)
                val columns = width / itemWidth
                binding.colorsRecyclerView.adapter = adapter
                binding.colorsRecyclerView.layoutManager = GridLayoutManager(requireContext(), columns)
            }
        })
    }
}