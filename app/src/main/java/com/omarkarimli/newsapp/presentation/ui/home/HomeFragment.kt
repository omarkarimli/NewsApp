package com.omarkarimli.newsapp.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.omarkarimli.newsapp.adapters.ArticleAdapter
import com.omarkarimli.newsapp.adapters.CategoryAdapter
import com.omarkarimli.newsapp.adapters.TrendingAdapter
import com.omarkarimli.newsapp.databinding.FragmentHomeBinding
import com.omarkarimli.newsapp.domain.models.CategoryModel
import com.omarkarimli.newsapp.menu.MorePopupMenuHandler
import com.omarkarimli.newsapp.data.source.local.categoryList
import com.omarkarimli.newsapp.utils.Constants
import com.omarkarimli.newsapp.utils.goneItem
import com.omarkarimli.newsapp.utils.visibleItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val categoryListCopy = categoryList.toMutableList().apply {
        add(
            0,
            CategoryModel(id = 0, image = null, name = Constants.ALL, desc = null, isSelected = true)
        )
    }

    @Inject
    lateinit var morePopupMenuHandler: MorePopupMenuHandler

    private val trendingAdapter = TrendingAdapter()
    private val categoryAdapter = CategoryAdapter()
    private val articleAdapter = ArticleAdapter()

    private val viewModel by viewModels<HomeViewModel>()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup adapters
        categoryAdapter.updateList(categoryListCopy)
        binding.rvTrending.adapter = trendingAdapter
        binding.rvCategories.adapter = categoryAdapter
        binding.rvArticles.adapter = articleAdapter

        // Setup UI listeners
        binding.editTextSearch.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment()
            findNavController().navigate(action)
        }

        binding.trendingSeeAll.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToTrendingFragment()
            findNavController().navigate(action)
        }

        trendingAdapter.onMoreClick = { context, anchorView, article ->
            morePopupMenuHandler.showPopupMenu(context, anchorView, article)
        }

        articleAdapter.onMoreClick = { context, anchorView, article ->
            morePopupMenuHandler.showPopupMenu(context, anchorView, article)
        }

        articleAdapter.onItemClick = { article ->
            article.url?.let {
                val action = HomeFragmentDirections.actionHomeFragmentToArticleFragment(it)
                findNavController().navigate(action)
            }
        }

        trendingAdapter.onItemClick = { article ->
            article.url?.let {
                val action = HomeFragmentDirections.actionHomeFragmentToArticleFragment(it)
                findNavController().navigate(action)
            }
        }

        categoryAdapter.onItemClick = { category ->
            if (!category.isSelected) {
                categoryAdapter.updateList(categoryListCopy.map {
                    it.copy(isSelected = it.name == category.name)
                })
                val query = if (category.name == Constants.ALL) Constants.EVERYTHING else category.name ?: Constants.EVERYTHING
                viewModel.fetchArticles(query)
            }
        }

        // Initial fetch once here, no longer in onResume
        if (savedInstanceState == null) {
            viewModel.fetchArticles(Constants.EVERYTHING)
        }

        observeData()
    }

    private fun observeData() {
        // Collect data flows with lifecycle awareness
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                launch {
                    viewModel.loading.collect { isLoading ->
                        if (isLoading) binding.progressBar.visibleItem() else binding.progressBar.goneItem()
                    }
                }
                launch {
                    viewModel.error.collect { errorMsg ->
                        Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_LONG).show()
                    }
                }
                launch {
                    viewModel.articles.collect { articles ->
                        articleAdapter.updateList(articles)
                        trendingAdapter.updateList(articles.take(Constants.TRENDING_VALUE))
                    }
                }
            }
        }
    }
}
