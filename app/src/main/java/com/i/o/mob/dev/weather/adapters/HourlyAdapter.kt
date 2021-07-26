package com.i.o.mob.dev.weather.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.i.o.mob.dev.weather.R
import com.i.o.mob.dev.weather.adapters.diffUtils.HourlyWeatherDiffUtils
import com.i.o.mob.dev.weather.data.enums.DateFormat
import com.i.o.mob.dev.weather.data.weather.Hourly
import com.i.o.mob.dev.weather.utils.Utils.Companion.insertInto
import com.i.o.mob.dev.weather.utils.Utils.Companion.setWeatherIcon
import com.i.o.mob.dev.weather.utils.Utils.Companion.toNormalTime
import kotlin.math.roundToInt


class HourlyAdapter : RecyclerView.Adapter<HourlyAdapter.ViewHolder>() {

    private val weather = mutableListOf<Hourly>()

    fun submitList(hourlyWeather: List<Hourly>) {
        val diffResult: DiffUtil.DiffResult =
            DiffUtil.calculateDiff(HourlyWeatherDiffUtils(this.weather, hourlyWeather))
        diffResult.dispatchUpdatesTo(this)
        this.weather.clear()
        this.weather.addAll(hourlyWeather)

    }

    override fun getItemCount(): Int = weather.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_hourly_weather, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.hourly = weather[position]
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val time = view.findViewById<TextView>(R.id.time)
        private val temperature = view.findViewById<TextView>(R.id.temperature)
        private val weatherIconAnim = view.findViewById<LottieAnimationView>(R.id.weatherIcon)

        var hourly: Hourly? = null
            set(value) {
                value.let { newValue ->
                    field = newValue
                    newValue?.apply {
                        time.text =
                            this.dt.toLong().toNormalTime(DateFormat.HoursMinutes).dropLast(3)
                                .insertInto(R.string.time_end)
                        temperature.text =
                            this.temp.roundToInt().toString().insertInto(R.string.degree_sign_end)
                        weatherIconAnim.setWeatherIcon(this.weather[0].id)
                    }
                }
            }
    }

}