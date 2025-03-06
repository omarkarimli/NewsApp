package com.omarkarimli.newsapp.presentation.ui.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.omarkarimli.newsapp.adapters.ArticleAdapter
import com.omarkarimli.newsapp.databinding.FragmentBookmarkBinding
import com.omarkarimli.newsapp.utils.MorePopupMenuHandler
import com.omarkarimli.newsapp.utils.goneItem
import com.omarkarimli.newsapp.utils.visibleItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BookmarkFragment : Fragment() {

    @Inject
    lateinit var morePopupMenuHandler: MorePopupMenuHandler

    private val articleAdapter = ArticleAdapter()

    private val viewModel by viewModels<BookmarkViewModel>()
    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

        viewModel.fetchArticles()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        articleAdapter.onMoreClick = { context, anchoredView, article ->
            morePopupMenuHandler.showPopupMenu(context, anchoredView, article)
        }

        binding.rvArticles.adapter = articleAdapter

        binding.editTextSearch.doOnTextChanged { inputText, _, _, _ ->
            val searchQuery = inputText.toString().trim()

            if (searchQuery.isNotEmpty()) {
                val searchedArticles = viewModel.filteredArticles.value.filter {
                    it.title?.contains(searchQuery, ignoreCase = true) == true
                }
                articleAdapter.updateList(searchedArticles)

                if (searchedArticles.isNotEmpty()) {
                    binding.rvArticles.visibleItem()
                    binding.empty.goneItem()
                } else {
                    binding.empty.visibleItem()
                    binding.rvArticles.goneItem()
                }
            } else {
                // Reset list when search is cleared
                viewModel.filteredArticles.value = viewModel.articles.value

                binding.empty.goneItem()
                binding.rvArticles.visibleItem()
            }
        }

        observeData()
    }

    private fun observeData() {
        // Launch a coroutine within the viewLifecycleOwner's lifecycleScope
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.filteredArticles.collect { filteredArticles ->
                    articleAdapter.updateList(filteredArticles)

                    if (filteredArticles.isNotEmpty()) {
                        binding.rvArticles.visibleItem()
                        binding.empty.goneItem()
                    } else {
                        binding.empty.visibleItem()
                        binding.rvArticles.goneItem()
                    }
                }
            }

            launch {
                viewModel.loading.collect { isLoading ->
                    binding.progressBar.apply {
                        if (isLoading) visibleItem() else goneItem()
                    }
                }
            }
        }
    }
}