package com.omarkarimli.newsapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.omarkarimli.newsapp.databinding.ItemArticleBinding
import com.omarkarimli.newsapp.domain.models.Article
import com.omarkarimli.newsapp.utils.diffUtils.ArticleDiffCallback
import com.omarkarimli.newsapp.utils.getTimeAgo
import com.omarkarimli.newsapp.utils.loadFromUrlToImage

class ArticleAdapter : ListAdapter<Article, ArticleAdapter.ArticleViewHolder>(ArticleDiffCallback()) {

    lateinit var onMoreClick: (Context, View, Article) -> Unit
    lateinit var onItemClick: (Article) -> Unit

    inner class ArticleViewHolder(val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ArticleViewHolder(
            ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = getItem(position)

        holder.binding.apply {
            imageViewArticle.loadFromUrlToImage(article.urlToImage)
            textViewSourceName.text = article.source?.name
            textViewNewsTitle.text = article.title
            textViewNewsAuthor.text = article.author
            textViewPublishedAt.text = article.publishedAt?.getTimeAgo()

            buttonMore.setOnClickListener { onMoreClick(it.context, it, article) }
            root.setOnClickListener   { onItemClick(article) }
        }
    }

    fun updateList(newList: List<Article>) {
        submitList(newList.toList())
    }
}
