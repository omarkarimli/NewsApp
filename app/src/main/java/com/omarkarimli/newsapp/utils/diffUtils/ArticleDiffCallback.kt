package com.omarkarimli.newsapp.utils.diffUtils

import androidx.recyclerview.widget.DiffUtil
import com.omarkarimli.newsapp.domain.models.Article

class ArticleDiffCallback : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem // Compare contents
    }
}
