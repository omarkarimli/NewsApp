package com.omarkarimli.newsapp.utils.diffUtils

import androidx.recyclerview.widget.DiffUtil
import com.omarkarimli.newsapp.domain.models.OnBoardingModel

class OnBoardingDiffCallback : DiffUtil.ItemCallback<OnBoardingModel>() {
    override fun areItemsTheSame(oldItem: OnBoardingModel, newItem: OnBoardingModel): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: OnBoardingModel, newItem: OnBoardingModel): Boolean {
        return oldItem == newItem // Compare contents
    }
}