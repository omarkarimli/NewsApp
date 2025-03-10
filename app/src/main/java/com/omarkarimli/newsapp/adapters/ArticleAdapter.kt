package com.omarkarimli.newsapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omarkarimli.newsapp.databinding.ItemArticleBinding
import com.omarkarimli.newsapp.domain.models.Article
import com.omarkarimli.newsapp.utils.getTimeAgo
import com.omarkarimli.newsapp.utils.loadFromUrlToImage

class ArticleAdapter : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    lateinit var onMoreClick: (context: Context, anchoredView: View, article: Article) -> Unit
    lateinit var onItemClick: (article: Article) -> Unit

    private var originalList = arrayListOf<Article>()

    inner class ArticleViewHolder(val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val layout = ItemArticleBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ArticleViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return originalList.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val instance = originalList[position]

        holder.binding.apply {
            imageViewArticle.loadFromUrlToImage(instance.urlToImage)
            textViewSourceName.text = instance.source?.name
            textViewNewsTitle.text = instance.title
            textViewNewsAuthor.text = instance.author
            textViewPublishedAt.text = getTimeAgo(instance.publishedAt!!)

            buttonMore.setOnClickListener { onMoreClick(it.context, it, instance) }

            root.setOnClickListener { onItemClick(instance) }
        }
    }

    fun updateList(newList: List<Article>) {
        originalList.clear()
        originalList.addAll(newList)
        notifyDataSetChanged()
    }
}
