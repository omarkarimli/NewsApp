package com.omarkarimli.newsapp.presentation.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.omarkarimli.newsapp.adapters.ArticleAdapter
import com.omarkarimli.newsapp.adapters.AuthorAdapter
import com.omarkarimli.newsapp.adapters.SearchCategoryAdapter
import com.omarkarimli.newsapp.databinding.FragmentSearchBinding
import com.omarkarimli.newsapp.utils.Constants
import com.omarkarimli.newsapp.menu.MorePopupMenuHandler
import com.omarkarimli.newsapp.data.source.local.categoryList
import com.omarkarimli.newsapp.utils.goneItem
import com.omarkarimli.newsapp.utils.visibleItem
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {

    @Inject
    lateinit var morePopupMenuHandler: MorePopupMenuHandler

    private val categoryAdapter = SearchCategoryAdapter()
    private val articleAdapter = ArticleAdapter()
    private val authorsAdapter = AuthorAdapter()

    private val viewModel by viewModels<SearchViewModel>()
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchCategories()
        viewModel.fetchArticles(Constants.EVERYTHING)
        viewModel.fetchAuthors()

        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }

        articleAdapter.onMoreClick = { context, anchoredView, article ->
            morePopupMenuHandler.showPopupMenu(context, anchoredView, article)
        }
        articleAdapter.onItemClick = { article ->
            if (article.url != null) {
                val action = SearchFragmentDirections.actionSearchFragmentToArticleFragment(article.url)
                findNavController().navigate(action)
            }
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        binding.rvCustom.adapter = articleAdapter
                    }
                    1 -> {
                        binding.rvCustom.adapter = categoryAdapter
                    }
                    2 -> {
                        binding.rvCustom.adapter = authorsAdapter
                    }
                }
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
        })

        // Default adapter
        binding.rvCustom.adapter = articleAdapter

        binding.editTextSearch.doOnTextChanged { inputText, _, _, _ ->
            val searchQuery = inputText.toString().trim()

            if (searchQuery.isNotEmpty()) {
                // Search in articles
                viewModel.fetchArticles(searchQuery)

                // Search in authors
                val searchedAuthors = viewModel.authors.value?.filter {
                    it.name?.contains(searchQuery, ignoreCase = true) == true
                } ?: emptyList()
                authorsAdapter.updateList(searchedAuthors)

                // Search in categories
                val searchedCategories = categoryList.filter {
                    it.name?.contains(searchQuery, ignoreCase = true) == true
                }
                categoryAdapter.updateList(searchedCategories)
            } else {
                // Reset articles
                viewModel.fetchArticles(Constants.EVERYTHING)

                // Reset authors
                viewModel.filteredAuthors.value = viewModel.authors.value

                // Reset categories
                viewModel.filteredCategories.value = categoryList
            }

            checkIfResultsAreEmpty()
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
        viewModel.empty.observe(viewLifecycleOwner) { isEmpty ->
            if (isEmpty) {
                binding.empty.visibleItem()
                binding.rvCustom.goneItem()
            } else {
                binding.empty.goneItem()
                binding.rvCustom.visibleItem()
            }
        }

        viewModel.articles.observe(viewLifecycleOwner) { articles ->
            articleAdapter.updateList(articles)
        }
        viewModel.filteredAuthors.observe(viewLifecycleOwner) { authors ->
            authorsAdapter.updateList(authors)
        }
        viewModel.filteredCategories.observe(viewLifecycleOwner) { categories ->
            categoryAdapter.updateList(categories)
        }
    }

    private fun checkIfResultsAreEmpty() {
        val isArticlesEmpty = viewModel.filteredAuthors.value.isNullOrEmpty()
        val isAuthorsEmpty = viewModel.filteredAuthors.value.isNullOrEmpty()
        val isCategoriesEmpty = viewModel.filteredCategories.value.isNullOrEmpty()

        val isResultEmpty = isArticlesEmpty && isAuthorsEmpty && isCategoriesEmpty
        binding.empty.apply {
            if (isResultEmpty) visibleItem() else goneItem()
        }
    }
}
