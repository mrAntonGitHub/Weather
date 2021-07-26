package com.i.o.mob.dev.weather.ui.chooseCity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.i.o.mob.dev.weather.R
import com.i.o.mob.dev.weather.adapters.CitiesAdapter
import com.i.o.mob.dev.weather.adapters.CitiesAdapterDelegate
import com.i.o.mob.dev.weather.data.enums.Cities
import com.i.o.mob.dev.weather.utils.DynamicSearchViewWatcher
import com.i.o.mob.dev.weather.utils.LinearLayoutManagerWrapper
import com.i.o.mob.dev.weather.utils.Utils.Companion.transparentStatusBar
import kotlinx.android.synthetic.main.fragment_choose_city.*
import kotlinx.coroutines.flow.collect

class ChooseCityFragment : Fragment(R.layout.fragment_choose_city),
    CitiesAdapterDelegate {

    private lateinit var viewModel: ChooseCityViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChooseCityViewModel::class.java)
        transparentStatusBar(false)

        focusSearchView()

        val citiesAdapter = CitiesAdapter()
        citiesAdapter.setDelegate(this)
        rv.layoutManager =
            LinearLayoutManagerWrapper(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv.adapter = citiesAdapter

        lifecycleScope.launchWhenCreated {
            viewModel.cities.collect {
                if (it.isEmpty()) {
                    citiesAdapter.submitList(it)
                    nothingToShow.visibility = View.VISIBLE
                } else {
                    citiesAdapter.submitList(it)
                    nothingToShow.visibility = View.GONE
                }
            }
        }

        searchView.setOnQueryTextListener(DynamicSearchViewWatcher(
            onTextChange = {
                it?.let { charSeq -> viewModel.search(charSeq) }
            },
            onTextSubmit = {}
        ))

        back.setOnClickListener {
            requireActivity().onBackPressed()
        }

    }

    private fun focusSearchView() {
        searchView.isIconified = true
        searchView.isIconified = false
    }

    override fun cityClicked(city: Cities) {
        viewModel.changeCity(city)
        findNavController().navigate(R.id.action_chooseCityFragment_to_weatherFragment)
    }
}