package com.omarkarimli.newsapp.presentation.ui.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
        viewModel.fetchArticles() // Ensure articles are loaded
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        articleAdapter.onMoreClick = { context, anchoredView, article ->
            morePopupMenuHandler.showPopupMenu(context, anchoredView, article)
        }

        binding.rvArticles.adapter = articleAdapter

        binding.editTextSearch.doOnTextChanged { inputText, _, _, _ ->
            val searchQuery = inputText.toString().trim()
            viewModel.updateFilteredArticles(searchQuery) // Moved filtering logic to ViewModel
        }

        observeData()
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.filteredArticles.collect { filteredArticles ->
                    articleAdapter.updateList(filteredArticles)
                    updateEmptyState(filteredArticles.isEmpty())
                }
            }

            launch {
                viewModel.loading.collect { isLoading ->
                    binding.progressBar.apply {
                        if (isLoading) visibleItem() else goneItem()
                    }
                }
            }

            launch {
                viewModel.error.collect { errorMessage ->
                    errorMessage?.let {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        viewModel.clearError() // Clear error after showing
                    }
                }
            }

            launch {
                viewModel.empty.collect { isEmpty ->
                    updateEmptyState(isEmpty)
                }
            }
        }
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        if (isEmpty) {
            binding.empty.visibleItem()
            binding.rvArticles.goneItem()
        } else {
            binding.empty.goneItem()
            binding.rvArticles.visibleItem()
        }
    }
}
