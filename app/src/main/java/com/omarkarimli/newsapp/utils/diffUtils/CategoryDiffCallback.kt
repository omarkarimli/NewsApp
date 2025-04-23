package com.omarkarimli.newsapp.utils.diffUtils

import androidx.recyclerview.widget.DiffUtil
import com.omarkarimli.newsapp.domain.models.CategoryModel

class CategoryDiffCallback : DiffUtil.ItemCallback<CategoryModel>() {
    override fun areItemsTheSame(oldItem: CategoryModel, newItem: CategoryModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CategoryModel, newItem: CategoryModel): Boolean {
        return oldItem == newItem // Compare contents
    }
}
