package com.omarkarimli.newsapp.utils.diffUtils

import androidx.recyclerview.widget.DiffUtil
import com.omarkarimli.newsapp.domain.models.SourceX

class AuthorDiffCallback : DiffUtil.ItemCallback<SourceX>() {
    override fun areItemsTheSame(oldItem: SourceX, newItem: SourceX): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SourceX, newItem: SourceX): Boolean {
        return oldItem == newItem // Compare contents
    }
}