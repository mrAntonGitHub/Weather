package com.i.o.mob.dev.weather.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Paint
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.i.o.mob.dev.weather.R
import com.i.o.mob.dev.weather.data.enums.DateFormat
import java.text.SimpleDateFormat
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class Utils {

    @Inject
    lateinit var context: Context

    init {
        com.i.o.mob.dev.weather.Application.application.appComponent.inject(this)
    }

    companion object {

        val context = Utils().context

        fun String.reviewDataToReadable(): String {
            return "${this.substring(8..9)}.${this.substring(5..6)}.${this.substring(0..3)}"
        }

        fun Fragment.hideKeyboard() {
            view?.let { activity?.hideKeyboard(it) }
        }

        fun Activity.hideKeyboard() {
            hideKeyboard(currentFocus ?: View(this))
        }

        private fun Context.hideKeyboard(view: View) {
            val inputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun Activity.transparentStatusBar(isTransparent: Boolean) {
            val window: Window = this.window
            if (isTransparent) {
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                )
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            }
        }

        fun Long.toNormalTime(dateFormat: DateFormat): String {
            val simpleDateFormat = SimpleDateFormat(dateFormat.pattern)
            return simpleDateFormat.format(this * 1000)
        }

        fun Fragment.transparentStatusBar(isTransparent: Boolean) {
            val window: Window = this.requireActivity().window
            if (isTransparent) {
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                )
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            }
        }

        fun setAnimation(animId: Int, context: Context): Animation =
            AnimationUtils.loadAnimation(context, animId)

        fun TextView.underline() {
            paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
        }

        fun String.insertInto(stringResource: Int): CharSequence? {
            return String.format(context.getString(stringResource), this)
        }

        fun getScreenWidthInPx() = Resources.getSystem().displayMetrics.widthPixels
        fun getScreenHeightInPx() = Resources.getSystem().displayMetrics.heightPixels

        fun LottieAnimationView.setWeatherIcon(weatherCode: Int) {
            when (weatherCode) {
                in 200..202 -> {
                    this.setAnimation("weather-storm.json")
                }
                in 210..221 -> {
                    this.setAnimation("weather-thunder.json")
                }
                in 230..232 -> {
                    this.setAnimation("weather-storm.json")
                }
                in 300..321 -> {
                    this.setAnimation("weather-storm.json")
                }
                in 500..504 -> {
                    this.setAnimation("weather-partly-shower.json")
                }
                511 -> {
                    this.setAnimation("weather-snow-sunny.json")
                }
                in 520..531 -> {
                    this.setAnimation("weather-storm.json")
                }
                in 600..601 -> {
                    this.setAnimation("weather-snow-sunny.json")
                }
                602 -> {
                    this.setAnimation("weather-snow.json")
                }
                in 611..621 -> {
                    this.setAnimation("weather-snow.json")
                }
                622 -> {
                    this.setAnimation("weather-snow.json")
                }
                in 701..761 -> {
                    this.setAnimation("weather-foggy.json")
                }
                762 -> {
                    this.setImageResource(R.drawable.ic_eruption)
                }
                771 -> {
                    this.setImageResource(R.drawable.ic_storm)
                }
                781 -> {
                    this.setImageResource(R.drawable.ic_tornado)
                }
                800 -> {
                    this.setAnimation("weather-sunny.json")
                }
                801 -> {
                    this.setAnimation("weather-partly-cloudy.json")
                }
                802 -> {
                    this.setAnimation("weather-windy.json")
                }
                in 803..804 -> {
                    this.setAnimation("weather-windy.json")
                }
            }
        }

    }

}