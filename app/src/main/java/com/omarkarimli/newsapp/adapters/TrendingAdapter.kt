package com.omarkarimli.newsapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.omarkarimli.newsapp.databinding.ItemTrendingBinding
import com.omarkarimli.newsapp.domain.models.Article
import com.omarkarimli.newsapp.utils.diffUtils.ArticleDiffCallback
import com.omarkarimli.newsapp.utils.getTimeAgo
import com.omarkarimli.newsapp.utils.loadFromUrlToImage

class TrendingAdapter : ListAdapter<Article, TrendingAdapter.TrendingViewHolder>(ArticleDiffCallback()) {

    lateinit var onMoreClick: (context: Context, anchoredView: View, article: Article) -> Unit
    lateinit var onItemClick: (article: Article) -> Unit

    private var originalList = arrayListOf<Article>()

    inner class TrendingViewHolder(val binding: ItemTrendingBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingViewHolder {
        val layout = ItemTrendingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TrendingViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return originalList.size
    }

    override fun onBindViewHolder(holder: TrendingViewHolder, position: Int) {
        val instance = originalList[position]

        holder.binding.apply {
            imageViewArticle.loadFromUrlToImage(instance.urlToImage)
            textViewSourceName.text = instance.source?.name
            textViewNewsTitle.text = instance.title
            textViewNewsAuthor.text = instance.author
            textViewPublishedAt.text = instance.publishedAt?.getTimeAgo()

            buttonMore.setOnClickListener { onMoreClick(it.context, it, instance) }
            root.setOnClickListener { onItemClick(instance) }
        }
    }

    fun updateList(newList: List<Article>) {
        originalList.clear()
        originalList.addAll(newList)
        submitList(newList)
    }
}
