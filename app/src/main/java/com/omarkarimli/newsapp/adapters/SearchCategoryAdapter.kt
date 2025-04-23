package com.omarkarimli.newsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.omarkarimli.newsapp.databinding.ItemSearchCategoryBinding
import com.omarkarimli.newsapp.domain.models.CategoryModel
import com.omarkarimli.newsapp.utils.diffUtils.CategoryDiffCallback

class SearchCategoryAdapter : ListAdapter<CategoryModel, SearchCategoryAdapter.SearchCategoryViewHolder>(CategoryDiffCallback()) {

    lateinit var onItemClick: (CategoryModel) -> Unit

    private var originalList = arrayListOf<CategoryModel>()

    inner class SearchCategoryViewHolder(val binding: ItemSearchCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchCategoryViewHolder {
        val layout = ItemSearchCategoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SearchCategoryViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return originalList.size
    }

    override fun onBindViewHolder(holder: SearchCategoryViewHolder, position: Int) {
        val instance = originalList[position]

        holder.binding.apply {
            if (instance.image != null) {
                imageViewCategory.setImageResource(instance.image)
            }
            if (instance.desc != null) {
                textViewCategoryDesc.text = instance.desc
            }

            textViewCategoryName.text = instance.name

            root.setOnClickListener { onItemClick(instance) }
        }
    }

    fun updateList(newList: List<CategoryModel>) {
        originalList.clear()
        originalList.addAll(newList)
        submitList(newList)
    }
}
