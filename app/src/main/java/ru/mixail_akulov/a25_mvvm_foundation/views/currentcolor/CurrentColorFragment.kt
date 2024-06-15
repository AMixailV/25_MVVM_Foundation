package ru.mixail_akulov.a25_mvvm_foundation.views.currentcolor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.mixail_akulov.a25_mvvm_foundation.databinding.FragmentCurrentColorBinding
import ru.mixail_akulov.a25_mvvm_foundation.views.base.BaseFragment
import ru.mixail_akulov.a25_mvvm_foundation.views.base.BaseScreen
import ru.mixail_akulov.a25_mvvm_foundation.views.base.screenViewModel

class CurrentColorFragment : BaseFragment() {

    // нет аргументов для этого экрана
    class Screen : BaseScreen

    override val viewModel by screenViewModel<CurrentColorViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentCurrentColorBinding.inflate(inflater, container, false)

        viewModel.currentColor.observe(viewLifecycleOwner) {
            binding.colorView.setBackgroundColor(it.value)
        }

        binding.changeColorButton.setOnClickListener {
            viewModel.changeColor()
        }

        return binding.root
    }
}