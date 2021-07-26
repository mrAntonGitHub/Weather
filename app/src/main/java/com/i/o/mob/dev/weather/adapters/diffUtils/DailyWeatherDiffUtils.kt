package com.i.o.mob.dev.weather.adapters.diffUtils

import androidx.recyclerview.widget.DiffUtil
import com.i.o.mob.dev.weather.data.weather.Daily
import com.i.o.mob.dev.weather.data.weather.Hourly

class DailyWeatherDiffUtils(
    private val newList: List<Daily>,
    private val oldList: List<Daily>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newList[newItemPosition].dt == oldList[oldItemPosition].dt
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newList[newItemPosition].temp == oldList[oldItemPosition].temp
                && newList[newItemPosition].feelsLike == oldList[oldItemPosition].feelsLike
    }

}