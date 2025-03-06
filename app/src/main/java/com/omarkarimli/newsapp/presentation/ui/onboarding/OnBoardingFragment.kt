package com.omarkarimli.newsapp.presentation.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.omarkarimli.newsapp.R
import com.omarkarimli.newsapp.adapters.OnBoardingAdapter
import com.omarkarimli.newsapp.databinding.FragmentOnBoardingBinding
import com.omarkarimli.newsapp.domain.models.OnBoardingModel

class OnBoardingFragment : Fragment() {

    private val adapter = OnBoardingAdapter()

    private var _binding: FragmentOnBoardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val onBoardingList = listOf(
            OnBoardingModel(
                R.drawable.onboarding1,
                "Lorem Ipsum is simply\ndummy",
                "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
            ),
            OnBoardingModel(
                R.drawable.onboarding2,
                "Lorem Ipsum is simply\ndummy",
                "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
            ),
            OnBoardingModel(
                R.drawable.onboarding3,
                "Lorem Ipsum is simply\ndummy",
                "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
            ),
        )

        adapter.updateList(onBoardingList)
        binding.viewPager2.adapter = adapter
        binding.dotsIndicator.attachTo(binding.viewPager2)

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == onBoardingList.size - 1) {
                    binding.buttonNext.text = "Get Started"
                } else {
                    binding.buttonNext.text = "Next"
                }
            }
        })

        binding.buttonNext.setOnClickListener {
            if (binding.viewPager2.currentItem < onBoardingList.size - 1) {
                binding.viewPager2.currentItem += 1
            } else {
                val action = OnBoardingFragmentDirections.actionOnBoardingFragmentToLoginFragment()
                findNavController().navigate(action)
            }
        }
        binding.buttonBack.setOnClickListener {
            if (binding.viewPager2.currentItem > 0) {
                binding.viewPager2.currentItem -= 1
            }
        }
    }
}