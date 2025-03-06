package com.omarkarimli.newsapp.presentation.ui.editProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.omarkarimli.newsapp.databinding.FragmentEditProfileBinding
import com.omarkarimli.newsapp.domain.models.UserData
import com.omarkarimli.newsapp.utils.goneItem
import com.omarkarimli.newsapp.utils.visibleItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    val args: EditProfileFragmentArgs by navArgs()

    private val viewModel by viewModels<EditProfileViewModel>()
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.close.setOnClickListener {
            viewModel.navigating.value = true
        }

        binding.done.setOnClickListener {
            val userData = UserData(
                name = binding.editTextName.text.toString().trim(),
                surname = binding.editTextSurname.text.toString().trim(),
                bio = binding.editTextBio.text.toString().trim(),
                website = binding.editTextWebsite.text.toString().trim()
            )
            viewModel.updateUserData(userData)
        }

        binding.apply {
            editTextName.setText(args.userData.name)
            editTextSurname.setText(args.userData.surname)
            editTextBio.setText(args.userData.bio)
            editTextWebsite.setText(args.userData.website)
        }

        observeData()
    }

    private fun observeData() {
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.apply {
                if (isLoading) visibleItem() else goneItem()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.navigating.observe(viewLifecycleOwner) { isNavigating ->
            if (isNavigating) {
                findNavController().navigateUp()
            }
        }
    }
}
