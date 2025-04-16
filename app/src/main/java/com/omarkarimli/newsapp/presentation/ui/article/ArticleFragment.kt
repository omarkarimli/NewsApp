package com.omarkarimli.newsapp.presentation.ui.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.omarkarimli.newsapp.databinding.FragmentArticleBinding
import com.omarkarimli.newsapp.utils.Constants
import com.omarkarimli.newsapp.menu.MorePopupMenuHandler
import com.omarkarimli.newsapp.utils.getTimeAgo
import com.omarkarimli.newsapp.utils.goneItem
import com.omarkarimli.newsapp.utils.loadFromUrlToImage
import com.omarkarimli.newsapp.utils.visibleItem
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ArticleFragment : Fragment() {

    private val args by navArgs<ArticleFragmentArgs>()

    @Inject
    lateinit var morePopupMenuHandler: MorePopupMenuHandler

    private val viewModel by viewModels<ArticleViewModel>()
    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

        viewModel.fetchArticle(args.url, Constants.EVERYTHING)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonMore.setOnClickListener {
            if (viewModel.article.value != null) {
                morePopupMenuHandler.showPopupMenu(it.context, it, viewModel.article.value!!)
            }
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

                findNavController().navigateUp()
            }
        }

        viewModel.article.observe(viewLifecycleOwner) { article ->
            binding.apply {
                imageViewArticle.loadFromUrlToImage(article.urlToImage!!)
                textViewNewsTitle.text = article.title
                textViewArticleDesc.text = article.description
                textViewPublishedAt.text = article.publishedAt?.getTimeAgo()
                textViewSourceName.text = article.source?.name
                textViewNewsAuthor.text = article.author
            }
        }
    }
}
