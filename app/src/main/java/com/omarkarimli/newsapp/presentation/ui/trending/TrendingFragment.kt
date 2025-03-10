package com.omarkarimli.newsapp.presentation.ui.trending

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.omarkarimli.newsapp.adapters.TrendingAdapter
import com.omarkarimli.newsapp.databinding.FragmentTrendingBinding
import com.omarkarimli.newsapp.utils.Constants
import com.omarkarimli.newsapp.menu.MorePopupMenuHandler
import com.omarkarimli.newsapp.utils.goneItem
import com.omarkarimli.newsapp.utils.visibleItem
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TrendingFragment : Fragment() {

    @Inject
    lateinit var morePopupMenuHandler: MorePopupMenuHandler

    private val trendingAdapter = TrendingAdapter()

    private val viewModel by viewModels<TrendingViewModel>()
    private var _binding: FragmentTrendingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrendingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

        viewModel.fetchArticles(Constants.EVERYTHING)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }

        trendingAdapter.onMoreClick = { context, anchoredView, article ->
            morePopupMenuHandler.showPopupMenu(context, anchoredView, article)
        }
        trendingAdapter.onItemClick = { article ->
            if (article.url != null) {
                val action = TrendingFragmentDirections.actionTrendingFragmentToArticleFragment(article.url)
                findNavController().navigate(action)
            }
        }

        binding.rvTrending.adapter = trendingAdapter

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

        viewModel.articles.observe(viewLifecycleOwner) { articles ->
            trendingAdapter.updateList(articles.take(Constants.TRENDING_VALUE))
        }
    }
}
