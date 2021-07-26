package com.i.o.mob.dev.weather.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.i.o.mob.dev.weather.adapters.diffUtils.CitiesDiffUtils
import com.i.o.mob.dev.weather.data.enums.Cities

interface CitiesAdapterDelegate{
    fun cityClicked(city: Cities)
}

class CitiesAdapter : RecyclerView.Adapter<CitiesAdapter.ViewHolder>() {

    private val cities = mutableListOf<Cities>()
    private var citiesAdapterDelegate: CitiesAdapterDelegate? = null

    fun setDelegate(citiesAdapterDelegate: CitiesAdapterDelegate) {
        this.citiesAdapterDelegate = citiesAdapterDelegate
    }

    fun submitList(cities: List<Cities>){
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(CitiesDiffUtils(this.cities, cities))
        diffResult.dispatchUpdatesTo(this)
        this.cities.clear()
        this.cities.addAll(cities)
    }

    override fun getItemCount(): Int = cities.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.city = cities[position]
        holder.apply {
            view.setOnClickListener {
                this.onItemClicked()
            }
        }
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        private val cityName = view.findViewById<TextView>(android.R.id.text1)

        var city : Cities? = null
        set(value) {
            value.let {newValue ->
                field = newValue
                newValue?.apply {
                    cityName.text = this.city
                }
            }
        }

        fun onItemClicked() {
            citiesAdapterDelegate?.cityClicked(cities[adapterPosition])
        }

    }

}