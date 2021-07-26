package com.i.o.mob.dev.weather.ui.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.i.o.mob.dev.weather.R
import com.i.o.mob.dev.weather.data.enums.Cities
import com.i.o.mob.dev.weather.utils.Utils.Companion.transparentStatusBar
import kotlinx.coroutines.delay

class SplashFragment : Fragment(R.layout.fragment_splash) {

    private lateinit var viewModel: SplashViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)

        transparentStatusBar(true)

        lifecycleScope.launchWhenCreated {
            val currentCity = viewModel.getCurrentCity()
            delay(2_000)
            if (currentCity == Cities.DEFAULT) {
                findNavController().navigate(R.id.action_splashFragment_to_chooseCityFragment)
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_weatherFragment)
            }
        }
    }

}