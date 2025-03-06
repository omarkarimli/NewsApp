package com.omarkarimli.newsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omarkarimli.newsapp.databinding.ItemCategoryBinding
import com.omarkarimli.newsapp.domain.models.Article
import com.omarkarimli.newsapp.domain.models.CategoryModel
import com.omarkarimli.newsapp.utils.goneItem
import com.omarkarimli.newsapp.utils.visibleItem

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    lateinit var onItemClick: (CategoryModel) -> Unit

    private var originalList = arrayListOf<CategoryModel>()

    inner class CategoryViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val layout = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CategoryViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return originalList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val instance = originalList[position]

        holder.binding.apply {
            textViewCategory.text = instance.name

            if (instance.isSelected) {
                divider.visibleItem()
            } else {
                divider.goneItem()
            }

            root.setOnClickListener { onItemClick(instance) }
        }
    }

    fun updateList(newList: List<CategoryModel>) {
        originalList.clear()
        originalList.addAll(newList)
        notifyDataSetChanged()
    }
}
