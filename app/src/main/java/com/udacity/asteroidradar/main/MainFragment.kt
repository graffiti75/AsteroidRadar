package com.udacity.asteroidradar.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

	private val viewModel: MainViewModel by lazy {
		ViewModelProvider(this)[MainViewModel::class.java]
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		val binding = FragmentMainBinding.inflate(inflater)
		binding.lifecycleOwner = this
		binding.viewModel = viewModel
		binding.asteroidRecycler.adapter = AsteroidAdapter(AsteroidAdapter.OnClickListener {
			viewModel.displayAsteroidDetails(it)
		})

		viewModel.navigateToSelectedAsteroid.observe(viewLifecycleOwner) {
			if (null != it) {
				findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
				viewModel.displayAsteroidDetailsComplete()
			}
		}

		/*
		viewModel.asteroidData.observe(viewLifecycleOwner) {
			if (null != it) {
				Log.i("nasa", "Asteroids: $it")
			}
		}
		 */

		viewModel.imageOfDayData.observe(viewLifecycleOwner) {
			if (null != it) {
				binding.activityMainImageOfTheDay.contentDescription = it.explanation
			}
		}

		setHasOptionsMenu(true)

		return binding.root
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		inflater.inflate(R.menu.main_overflow_menu, menu)
		super.onCreateOptionsMenu(menu, inflater)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		return true
	}
}