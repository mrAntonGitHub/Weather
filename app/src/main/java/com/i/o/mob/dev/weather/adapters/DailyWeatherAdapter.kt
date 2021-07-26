package com.i.o.mob.dev.weather.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.i.o.mob.dev.weather.R
import com.i.o.mob.dev.weather.adapters.diffUtils.DailyWeatherDiffUtils
import com.i.o.mob.dev.weather.data.enums.DateFormat
import com.i.o.mob.dev.weather.data.weather.Daily
import com.i.o.mob.dev.weather.utils.Utils.Companion.setWeatherIcon
import com.i.o.mob.dev.weather.utils.Utils.Companion.toNormalTime
import kotlinx.android.synthetic.main.item_daily_weather.view.*
import kotlin.math.roundToInt


class DailyWeatherAdapter : RecyclerView.Adapter<DailyWeatherAdapter.ViewHolder>() {

    var expandedPosition = -1
    var previousExpandedPosition = -1
    private val weather = mutableListOf<Daily>()

    fun submitList(dailyWeather: List<Daily>) {
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            DailyWeatherDiffUtils(
                this.weather,
                dailyWeather
            )
        )
        diffResult.dispatchUpdatesTo(this)
        this.weather.clear()
        this.weather.addAll(dailyWeather)

    }

    override fun getItemCount(): Int = weather.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_daily_weather,
                parent,
                false
            )
        )
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.expandedItems.visibility = View.GONE
        holder.itemView.isActivated = false
        expandedPosition = -1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val isExpanded = position == expandedPosition
        holder.itemView.expandedItems.visibility = if (isExpanded) View.VISIBLE else View.GONE
        holder.itemView.isActivated = isExpanded

        if (isExpanded) previousExpandedPosition = position

        holder.itemView.setOnClickListener {
            expandedPosition = if (isExpanded) -1 else position
            notifyItemChanged(previousExpandedPosition)
            notifyItemChanged(position)
        }

        holder.daily = weather[position]
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvDay: TextView = itemView.findViewById(R.id.tvDay)
        private val tvDataOfTheDay: TextView = itemView.findViewById(R.id.tvDayDate)
        private val tvDayTemperature: TextView = itemView.findViewById(R.id.tvDailyTemperature)
        private val ivDayIcon: LottieAnimationView = itemView.findViewById(R.id.ivDailyWeatherIcon)
        private val tvDailyHumidityValue: TextView =
            itemView.findViewById(R.id.tvDailyHumidityValue)
        private val tvDailyPressureValue: TextView =
            itemView.findViewById(R.id.tvDailyPressureValue)
        private val tvDailyProbabilityOfPrecipitationValue: TextView =
            itemView.findViewById(R.id.tvDailyProbabilityOfPrecipitationValue)
        private val tvDailyCloudsValue: TextView = itemView.findViewById(R.id.tvDailyCloudsValue)
        private val tvDailyWindValue: TextView = itemView.findViewById(R.id.tvDailyWindValue)
        private val tvDailyPrecipitationValue: TextView =
            itemView.findViewById(R.id.tvDailyPrecipitationValue)
        private val tvDailyDewPointValue: TextView =
            itemView.findViewById(R.id.tvDailyDewPointValue)
        private val tvDailyUviValue: TextView = itemView.findViewById(R.id.tvDailyUviValue)

        private val expandedItems: ConstraintLayout = itemView.findViewById(R.id.expandedItems)

        private val tvDailyMorningTemperature: TextView =
            itemView.findViewById(R.id.tvDailyMorningTemperature)
        private val tvDailyAfternoonTemperature: TextView =
            itemView.findViewById(R.id.tvDailyAfternoonTemperature)
        private val tvDailyEveningTemperature: TextView =
            itemView.findViewById(R.id.tvDailyEveningTemperature)
        private val tvDailyNightTemperature: TextView =
            itemView.findViewById(R.id.tvDailyNightTemperature)

        private val ivDailyMorningIcon: LottieAnimationView =
            itemView.findViewById(R.id.ivDailyMorningIcon)
        private val ivDailyAfternoonIcon: LottieAnimationView =
            itemView.findViewById(R.id.ivDailyAfternoonIcon)
        private val ivDailyEveningIcon: LottieAnimationView =
            itemView.findViewById(R.id.ivDailyEveningIcon)
        private val ivDailyNightIcon: LottieAnimationView =
            itemView.findViewById(R.id.ivDailyNightIcon)

        var daily: Daily? = null
            set(value) {
                value.let { newValue ->
                    field = newValue
                    newValue?.apply {
                        tvDay.text = dt.toLong().toNormalTime(DateFormat.NameOfDaysOfTheWeek)
                        tvDataOfTheDay.text = dt.toLong().toNormalTime(DateFormat.DayMonth)
                        tvDayTemperature.text = String.format(
                            itemView.resources.getString(
                                R.string.degree_sign_end,
                                temp.day.roundToInt().toString()
                            )
                        )
                        ivDayIcon.setWeatherIcon(weather[0].id)
                        tvDailyHumidityValue.text = String.format(
                            itemView.resources.getString(R.string.percent_sign_end),
                            humidity
                        )
                        tvDailyPressureValue.text = String.format(
                            itemView.resources.getString(
                                R.string.meter_pressure,
                                pressure.toString()
                            )
                        )
                        tvDailyProbabilityOfPrecipitationValue.text = String.format(
                            itemView.resources.getString(
                                R.string.percent_sign_end
                            ), pop
                        )
                        tvDailyCloudsValue.text = String.format(
                            itemView.resources.getString(R.string.percent_sign_end),
                            clouds
                        )
                        tvDailyWindValue.text = String.format(
                            itemView.resources.getString(R.string.meter_per_second),
                            windSpeed
                        )
                        tvDailyPrecipitationValue.text = String.format(
                            itemView.resources.getString(
                                R.string.percent_sign_end,
                                humidity.toString()
                            )
                        )
                        tvDailyDewPointValue.text = String.format(
                            itemView.resources.getString(
                                R.string.degree_sign_end,
                                dewPoint.toString()
                            )
                        )
                        tvDailyUviValue.text = uvi.toString()
                        tvDailyMorningTemperature.text = String.format(
                            itemView.resources.getString(
                                R.string.degree_sign_end,
                                temp.morn.roundToInt().toString()
                            )
                        )
                        tvDailyAfternoonTemperature.text = String.format(
                            itemView.resources.getString(
                                R.string.degree_sign_end,
                                temp.day.roundToInt().toString()
                            )
                        )
                        tvDailyEveningTemperature.text = String.format(
                            itemView.resources.getString(
                                R.string.degree_sign_end,
                                temp.eve.roundToInt().toString()
                            )
                        )
                        tvDailyNightTemperature.text = String.format(
                            itemView.resources.getString(
                                R.string.degree_sign_end,
                                temp.night.roundToInt().toString()
                            )
                        )

                        ivDailyMorningIcon.setWeatherIcon(weather[0].id)
                        ivDailyAfternoonIcon.setWeatherIcon(weather[0].id)
                        ivDailyEveningIcon.setWeatherIcon(weather[0].id)
                        ivDailyNightIcon.setWeatherIcon(weather[0].id)
                    }
                }
            }
    }

}