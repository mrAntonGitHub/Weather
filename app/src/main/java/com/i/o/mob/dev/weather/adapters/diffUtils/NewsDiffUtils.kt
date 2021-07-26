package com.i.o.mob.dev.weather.adapters.diffUtils

import androidx.recyclerview.widget.DiffUtil
import com.i.o.mob.dev.weather.data.enums.Cities
import com.i.o.mob.dev.weather.data.news.Article
import com.i.o.mob.dev.weather.data.news.News

class NewsDiffUtils(
    private val newList: List<Article>,
    private val oldList: List<Article>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newList[newItemPosition].url == oldList[oldItemPosition].url
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newList[newItemPosition].author == oldList[oldItemPosition].author
                && newList[newItemPosition].title == oldList[oldItemPosition].title
                && newList[newItemPosition].publishedAt == oldList[oldItemPosition].publishedAt
    }

}