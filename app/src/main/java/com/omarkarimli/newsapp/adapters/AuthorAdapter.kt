package com.omarkarimli.newsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.omarkarimli.newsapp.databinding.ItemAuthorBinding
import com.omarkarimli.newsapp.domain.models.SourceX
import com.omarkarimli.newsapp.utils.diffUtils.AuthorDiffCallback

class AuthorAdapter : ListAdapter<SourceX, AuthorAdapter.AuthorViewHolder>(AuthorDiffCallback()){

    private var originalList = arrayListOf<SourceX>()

    inner class AuthorViewHolder(val binding: ItemAuthorBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuthorViewHolder {
        val layout = ItemAuthorBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AuthorViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return originalList.size
    }

    override fun onBindViewHolder(holder: AuthorViewHolder, position: Int) {
        val instance = originalList[position]

        holder.binding.apply {
            textViewSourceName.text = instance.name
            textViewSourceDesc.text = instance.description
        }
    }

    fun updateList(newList: List<SourceX>) {
        originalList.clear()
        originalList.addAll(newList)
        submitList(newList)
    }
}
