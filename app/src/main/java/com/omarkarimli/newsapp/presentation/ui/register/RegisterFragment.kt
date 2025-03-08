package com.omarkarimli.newsapp.presentation.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.omarkarimli.newsapp.databinding.FragmentRegisterBinding
import com.omarkarimli.newsapp.utils.goneItem
import com.omarkarimli.newsapp.utils.visibleItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private val viewModel by viewModels<RegisterViewModel>()
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.back.setOnClickListener { viewModel.isNavigating.postValue(true) }
        binding.signInLink.setOnClickListener { viewModel.isNavigating.postValue(true) }

        binding.buttonSignup.setOnClickListener { validateAndRegister() }

        observeData()
    }

    private fun validateAndRegister() {
        val email = binding.editTextEmail.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()
        val name = binding.editTextName.text.toString().trim()
        val surname = binding.editTextSurname.text.toString().trim()
        val bio = binding.editTextBio.text.toString().trim()
        val website = binding.editTextWebsite.text.toString().trim()

        // Field validation
        if (email.isEmpty()) {
            binding.textInputLayoutEmail.error = "Email cannot be empty"
            return
        } else {
            binding.textInputLayoutEmail.error = null
        }

        if (password.isEmpty()) {
            binding.textInputLayoutPassword.error = "Password cannot be empty"
            return
        } else {
            binding.textInputLayoutPassword.error = null
        }

        if (name.isEmpty()) {
            binding.textInputLayoutName.error = "Name cannot be empty"
            return
        } else {
            binding.textInputLayoutName.error = null
        }

        if (surname.isEmpty()) {
            binding.textInputLayoutSurname.error = "Surname cannot be empty"
            return
        } else {
            binding.textInputLayoutSurname.error = null
        }

        if (bio.isEmpty()) {
            binding.textInputLayoutBio.error = "Bio cannot be empty"
            return
        } else {
            binding.textInputLayoutBio.error = null
        }

        if (website.isEmpty()) {
            binding.textInputLayoutWebsite.error = "Website cannot be empty"
            return
        } else {
            binding.textInputLayoutWebsite.error = null
        }

        viewModel.registerNewUser(email, password, name, surname, bio, website)
    }

    private fun observeData() {
        viewModel.isNavigating.observe(viewLifecycleOwner) { isNavigating ->
            if (isNavigating) {
                findNavController().navigateUp()
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.apply {
                if (isLoading) visibleItem() else goneItem()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.success.observe(viewLifecycleOwner) { successMessage ->
            if (!successMessage.isNullOrEmpty()) {
                Toast.makeText(requireContext(), successMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
