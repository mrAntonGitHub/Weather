package com.i.o.mob.dev.weather.ui.home

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.i.o.mob.dev.weather.R
import com.i.o.mob.dev.weather.adapters.DailyWeatherAdapter
import com.i.o.mob.dev.weather.adapters.HourlyAdapter
import com.i.o.mob.dev.weather.adapters.NewsAdapter
import com.i.o.mob.dev.weather.adapters.NewsDelegate
import com.i.o.mob.dev.weather.adapters.decorators.VerticalSpaceItemDecorator
import com.i.o.mob.dev.weather.data.enums.DateFormat
import com.i.o.mob.dev.weather.data.news.Article
import com.i.o.mob.dev.weather.data.weather.Current
import com.i.o.mob.dev.weather.data.weather.Daily
import com.i.o.mob.dev.weather.data.weather.WeatherForecast
import com.i.o.mob.dev.weather.utils.State
import com.i.o.mob.dev.weather.utils.Utils.Companion.insertInto
import com.i.o.mob.dev.weather.utils.Utils.Companion.setAnimation
import com.i.o.mob.dev.weather.utils.Utils.Companion.setWeatherIcon
import com.i.o.mob.dev.weather.utils.Utils.Companion.toNormalTime
import com.i.o.mob.dev.weather.utils.Utils.Companion.transparentStatusBar
import com.i.o.mob.dev.weather.utils.Utils.Companion.underline
import kotlinx.android.synthetic.main.fragment_weather.*
import kotlinx.android.synthetic.main.item_header.*
import kotlinx.coroutines.flow.collect
import kotlin.math.roundToInt
import kotlin.random.Random


class WeatherFragment : Fragment(R.layout.fragment_weather), NewsDelegate {

    private lateinit var viewModel: WeatherViewModel

    private val hourlyAdapter = HourlyAdapter()
    private val dailyWeatherAdapter = DailyWeatherAdapter()
    private val newsAdapter = NewsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        transparentStatusBar(false)

        hourlyRv.adapter = hourlyAdapter
        hourlyRv.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL))
        hourlyRv.addItemDecoration(VerticalSpaceItemDecorator(40))

        newsAdapter.setDelegate(this)
        newsRecyclerView.adapter = newsAdapter

        rvDailyWeatherList.adapter = dailyWeatherAdapter
        rvDailyWeatherList.setHasFixedSize(true)

        lifecycleScope.launchWhenStarted {
            viewModel.weather.collect {
                when (it) {
                    is State.Success -> {
                        loadUi(it.data, true)
                        reloadWeather.clearAnimation()
                        showSnackbar(false)
                        tvCantLoadMessage.isVisible = false
                        tvCantLoadMessage.visibility = View.GONE
                        hourlyAdapter.submitList(it.data.hourly)
                        dailyWeatherAdapter.submitList(it.data.daily)
                    }
                    is State.SuccessFromDb -> {
                        loadUi(it.data, false)
                        showSnackbar(true)
                        tvCantLoadMessage.isVisible = false
                        reloadWeather.clearAnimation()
                        tvCantLoadMessage.visibility = View.GONE
                        hourlyAdapter.submitList(it.data.hourly)
                        dailyWeatherAdapter.submitList(it.data.daily)
                    }
                    is State.Loading -> {
                        tvCantLoadMessage.isVisible = false
                        reloadWeather.startAnimation(
                            setAnimation(
                                R.anim.rotation,
                                requireActivity()
                            )
                        )
                    }
                    is State.Error -> {
                        tvCantLoadMessage.isVisible = true
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.news.collect {
                when (it) {
                    is State.Success -> {
                        shouldGoOut.isVisible = true
                        newsAdapter.submitList(it.data.articles)
                    }
                    is State.SuccessFromDb -> {
                        /* No need to use */
                    }
                    is State.Loading -> {
                        shouldGoOut.isVisible = false
                    }
                    is State.Error -> {
                        shouldGoOut.isVisible = false
                    }
                }
            }
        }

        reloadWeather.setOnClickListener {
            viewModel.updateWeather()
        }

        appBar.setOnClickListener {
            val expandedAppBarHeight =
                resources.getDimension(R.dimen.main_activity_header_expanded_height).toInt()
            expandAppBar(appBar.layoutParams.height != expandedAppBarHeight)
        }

        tvCurrentCity.setOnClickListener {
            findNavController().navigate(R.id.action_weatherFragment_to_chooseCityFragment)
        }

        btnShowMenu.setOnClickListener {
            setPopMenu(it)
        }

    }

    private fun expandAppBar(shouldExpand: Boolean) {
        val propertyHeightName = "layout_height"
        val expandDuration: Long = 600
        if (shouldExpand) {
            val propertyHeightUp = PropertyValuesHolder.ofInt(
                propertyHeightName,
                resources.getDimension(
                    R.dimen.main_activity_header_height
                ).toInt(),
                resources.getDimension(R.dimen.main_activity_header_expanded_height).toInt()
            )

            val valueAnimator = ValueAnimator.ofPropertyValuesHolder(propertyHeightUp).apply {
                duration = expandDuration
                addUpdateListener {
                    appBar?.layoutParams?.height = it.getAnimatedValue(propertyHeightName) as Int
                    appBar?.requestLayout()
                }
            }
            valueAnimator.start()
            tvShowMore.text = getString(R.string.hide)
            tvShowMore.underline()
        } else {
            val propertyHeightDown = PropertyValuesHolder.ofInt(
                propertyHeightName, resources.getDimension(
                    R.dimen.main_activity_header_expanded_height
                ).toInt(), resources.getDimension(R.dimen.main_activity_header_height).toInt()
            )
            val valueAnimator = ValueAnimator.ofPropertyValuesHolder(propertyHeightDown).apply {
                duration = expandDuration
                addUpdateListener {
                    appBar?.layoutParams?.height = it.getAnimatedValue(propertyHeightName) as Int
                    appBar?.requestLayout()
                }
            }
            valueAnimator.start()
            tvShowMore.text = getString(R.string.show_more)
            tvShowMore.underline()
        }
    }

    private fun setPopMenu(view: View) {
        val popupMenu = PopupMenu(requireActivity(), view)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings -> {
                    true
                }
                else -> false
            }
        }
        popupMenu.inflate(R.menu.menu_main)
        popupMenu.show()
    }

    private fun loadUi(weatherForecast: WeatherForecast, gotDataFromAPI: Boolean) {
        tvCurrentCity.text = viewModel.getCurrentCity().city
        setWeatherHeader(weatherForecast, gotDataFromAPI)
    }

    private fun setWeatherHeader(weatherForecast: WeatherForecast, gotDataFromAPI: Boolean) {
        weatherForecast.apply {
            val currentForecast = current
            val dailyForecast = daily

            tvCurrentTemperature.text =
                currentForecast.temp.roundToInt().toString().insertInto(R.string.degree_sign_end)
            tvFeelsLike.text =
                currentForecast.feelsLike.roundToInt().toString().insertInto(R.string.feels_like)
            tvPrecipitation.text = (dailyForecast[0].pop * 100).toInt().toString()
                .insertInto(R.string.probability_of_precipitation)
            tvCurrentWeatherDescription.text =
                viewModel.determineWeatherCode(weatherForecast.current.weather[0].id)?.descriptionRu
            ivCurrentWeatherIcon.setWeatherIcon(currentForecast.weather[0].id)
            ivCurrentWeatherIcon.playAnimation()

            val avatarsList = viewModel.determineWeatherCondition(current)?.listOfIcons
            avatarsList?.get(Random.nextInt(0, avatarsList.count())).let {
                it?.let { it1 ->
                    ivAvatar.setImageResource(
                        it1
                    )
                }
            }

            if (gotDataFromAPI) {
                tvLastUpdate.text = "Обновлено только что"
            } else {
                tvLastUpdate.text = "Данные устарели"
            }

            setExpandedHeader(currentForecast, dailyForecast)
        }
    }

    private fun setExpandedHeader(currentForecast: Current, dailyForecast: List<Daily>) {
        tvMorningTemperature.text =
            dailyForecast[0].temp.morn.roundToInt().toString().insertInto(R.string.degree_sign_end)
        tvAfternoonTemperature.text =
            dailyForecast[0].temp.day.roundToInt().toString().insertInto(R.string.degree_sign_end)
        tvEveningTemperature.text =
            dailyForecast[0].temp.eve.roundToInt().toString().insertInto(R.string.degree_sign_end)
        tvNightTemperature.text =
            dailyForecast[0].temp.night.roundToInt().toString().insertInto(R.string.degree_sign_end)
        tvHumidityValue.text =
            currentForecast.humidity.toString().insertInto(R.string.percent_sign_end)
        tvPressureValue.text = (currentForecast.pressure.toString() + " гПа.")
        tvVisibilityValue.text =
            currentForecast.visibility.toString().insertInto(R.string.meter_sign_end)
        tvCloudsValue.text = currentForecast.clouds.toString().insertInto(R.string.percent_sign_end)
        tvWindSpeedValue.text =
            currentForecast.windSpeed.toString().insertInto(R.string.meter_per_second)

        when (currentForecast.windDeg) {
            in 315 downTo 45 -> {
                tvWindDirectionValue.text = resources.getString(R.string.north)
            }
            in 45..135 -> {
                tvWindDirectionValue.text = resources.getString(R.string.east)
            }
            in 135..225 -> {
                tvWindDirectionValue.text = resources.getString(R.string.south)
            }
            in 225..315 -> {
                tvWindDirectionValue.text = resources.getString(R.string.west)
            }
        }
        tvDewPointValue.text =
            currentForecast.dewPoint.toString().insertInto(R.string.degree_sign_end)
        tvUviValue.text = currentForecast.uvi.toString()
        tvSunrise.text = currentForecast.sunrise.toLong().toNormalTime(DateFormat.HoursMinutes)
        tvSunset.text = currentForecast.sunset.toLong().toNormalTime(DateFormat.HoursMinutes)
    }

    private fun showSnackbar(visibility: Boolean) {
        if (visibility) {
            snackbar.show()
        } else {
            snackbar.dismiss()
            tvCantLoadMessage.visibility = View.GONE
        }
    }

    private val snackbar: Snackbar by lazy {
        val snack =
            Snackbar.make(clRootLayout, "Данные устарели", Snackbar.LENGTH_INDEFINITE).setAction(
                "Обновить"
            ) {
                viewModel.updateWeather()
            }
        snack.setActionTextColor(getColor(requireActivity(), R.color.snackBar_actionTextColor))
        snack.setBackgroundTint(getColor(requireActivity(), R.color.white))
        snack.setTextColor(getColor(requireActivity(), R.color.colorBlack))
        snack
    }

    override fun articleClicked(article: Article) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
        startActivity(browserIntent)
    }
}