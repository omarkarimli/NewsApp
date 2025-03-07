package com.omarkarimli.newsapp.presentation.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.omarkarimli.newsapp.adapters.ArticleAdapter
import com.omarkarimli.newsapp.adapters.SearchCategoryAdapter
import com.omarkarimli.newsapp.databinding.FragmentExploreBinding
import com.omarkarimli.newsapp.utils.Constants
import com.omarkarimli.newsapp.utils.MorePopupMenuHandler
import com.omarkarimli.newsapp.utils.goneItem
import com.omarkarimli.newsapp.utils.visibleItem
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ExploreFragment : Fragment() {

    @Inject
    lateinit var morePopupMenuHandler: MorePopupMenuHandler

    private val categoryAdapter = SearchCategoryAdapter()
    private val articleAdapter = ArticleAdapter()

    private val viewModel by viewModels<ExploreViewModel>()
    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

        viewModel.fetchArticles(Constants.EVERYTHING)
        viewModel.fetchCategories()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        articleAdapter.onMoreClick = { context, anchoredView, article ->
            morePopupMenuHandler.showPopupMenu(context, anchoredView, article)
        }
        articleAdapter.onItemClick = { article ->
            if (article.url != null) {
                val action = ExploreFragmentDirections.actionExploreFragmentToArticleFragment(article.url)
                findNavController().navigate(action)
            }
        }

        binding.rvArticles.adapter = articleAdapter
        binding.rvCategories.adapter = categoryAdapter

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
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.articles.observe(viewLifecycleOwner) { articles ->
            articleAdapter.updateList(articles)
        }
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            categoryAdapter.updateList(categories)
        }
    }
}