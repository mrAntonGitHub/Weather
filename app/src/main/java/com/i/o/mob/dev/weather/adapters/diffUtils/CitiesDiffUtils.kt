package com.i.o.mob.dev.weather.adapters.diffUtils

import androidx.recyclerview.widget.DiffUtil
import com.i.o.mob.dev.weather.data.enums.Cities

class CitiesDiffUtils(
    private val newList: List<Cities>,
    private val oldList: List<Cities>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newList[newItemPosition].city == oldList[oldItemPosition].city
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newList[newItemPosition].city == oldList[oldItemPosition].city
                && newList[newItemPosition].lat == oldList[oldItemPosition].lat
    }

}