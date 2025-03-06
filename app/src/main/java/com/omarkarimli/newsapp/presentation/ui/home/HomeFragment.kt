package com.omarkarimli.newsapp.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.omarkarimli.newsapp.R
import com.omarkarimli.newsapp.adapters.ArticleAdapter
import com.omarkarimli.newsapp.adapters.CategoryAdapter
import com.omarkarimli.newsapp.adapters.TrendingAdapter
import com.omarkarimli.newsapp.databinding.FragmentHomeBinding
import com.omarkarimli.newsapp.domain.models.CategoryModel
import com.omarkarimli.newsapp.utils.Constants
import com.omarkarimli.newsapp.utils.MorePopupMenuHandler
import com.omarkarimli.newsapp.utils.categoryList
import com.omarkarimli.newsapp.utils.goneItem
import com.omarkarimli.newsapp.utils.visibleItem
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

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

    override fun onResume() {
        super.onResume()

        viewModel.fetchArticles(Constants.EVERYTHING)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editTextSearch.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment()
            findNavController().navigate(action)
        }
        binding.trendingSeeAll.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToTrendingFragment()
            findNavController().navigate(action)
        }

        val categoryListCopy = categoryList.toMutableList().apply {
            add(
                0,
                CategoryModel(id = 0, image = null, name = "All", desc = null, isSelected = true)
            )
        }
        categoryAdapter.updateList(categoryListCopy)

        trendingAdapter.onMoreClick = { context, anchoredView, article ->
            morePopupMenuHandler.showPopupMenu(context, anchoredView, article)
        }
        articleAdapter.onMoreClick = { context, anchoredView, article ->
            morePopupMenuHandler.showPopupMenu(context, anchoredView, article)
        }
        categoryAdapter.onItemClick = { category ->
            // Update Categories due to Divider visibility
            categoryAdapter.updateList(categoryList.map {
                it.copy(isSelected = it.name == category.name)
            })

            // Update Articles
            if (category.name == "All") {
                viewModel.fetchArticles(Constants.EVERYTHING)
            } else {
                viewModel.fetchArticles(category.name!!)
            }
        }

        binding.rvTrending.adapter = trendingAdapter
        binding.rvCategories.adapter = categoryAdapter
        binding.rvArticles.adapter = articleAdapter

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
            articleAdapter.updateList(articles)
            trendingAdapter.updateList(articles.take(Constants.TRENDING_VALUE))
        }
    }
}
