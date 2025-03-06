package com.omarkarimli.newsapp.presentation.ui.settings

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.color.MaterialColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.materialswitch.MaterialSwitch
import com.omarkarimli.newsapp.R
import com.omarkarimli.newsapp.databinding.FragmentSettingsBinding
import com.omarkarimli.newsapp.utils.goneItem
import com.omarkarimli.newsapp.utils.visibleItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private val viewModel by viewModels<SettingsViewModel>()

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

        viewModel.initializeDarkModeState()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.layoutLogout.setOnClickListener {
            buildAlertDialog(requireContext())
        }

        binding.switchDarkMode.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.changeDarkModeState(isChecked)

            changeSwitchUI(binding.switchDarkMode, isChecked)
            applyTheme(isChecked)
        }

        observeData()
    }

    private fun observeData() {

        viewModel.loading.observe(viewLifecycleOwner) {
            if (it) {
                binding.progressBar.visibleItem()
            } else {
                binding.progressBar.goneItem()
            }
        }
        viewModel.error.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isNavigating.observe(viewLifecycleOwner) { isNavigating ->
            if (isNavigating) {
                val action = SettingsFragmentDirections.actionSettingsFragmentToLoginFragment()
                findNavController().navigate(action)
            }
        }

        viewModel.isDarkMode.observe(viewLifecycleOwner) { isChecked ->
            binding.switchDarkMode.isChecked = isChecked
        }
    }

    private fun applyTheme(isChecked: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    private fun changeSwitchUI(switch: MaterialSwitch, isChecked: Boolean) {
        val thumbTint = MaterialColors.getColor(switch,
            if (isChecked)
                com.google.android.material.R.attr.colorSecondaryContainer
            else
                com.google.android.material.R.attr.colorOnSecondaryContainer
        )

        val trackTint = MaterialColors.getColor(switch,
            if (isChecked)
                com.google.android.material.R.attr.colorOnSecondaryContainer
            else
                com.google.android.material.R.attr.colorSecondaryContainer
        )

        switch.thumbTintList = ColorStateList.valueOf(thumbTint)
        switch.trackTintList = ColorStateList.valueOf(trackTint)
    }

    private fun buildAlertDialog(context: Context) {
        MaterialAlertDialogBuilder(context)
            .setTitle("Are you sure?")
            .setMessage("You will logout and redirect to login screen")
            .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->
                // Respond to neutral button press
            }
            .setPositiveButton(resources.getString(R.string.yes)) { dialog, which ->
                viewModel.signOutAndRedirect()
            }
            .show()
    }
}
