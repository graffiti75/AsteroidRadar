package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

	private lateinit var binding : FragmentMainBinding

	private val viewModel: MainViewModel by lazy {
		ViewModelProvider(this)[MainViewModel::class.java]
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentMainBinding.inflate(inflater)
		binding.lifecycleOwner = this
		binding.viewModel = viewModel

		binding.asteroidRecycler.adapter = AsteroidAdapter(AsteroidAdapter.OnClickListener {
			viewModel.displayAsteroidDetails(it)
		})

		observeListeners()
		setHasOptionsMenu(true)

		return binding.root
	}

	private fun observeListeners() {
		viewModel.navigateToSelectedAsteroid.observe(viewLifecycleOwner) {
			if (null != it) {
				findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
				viewModel.displayAsteroidDetailsComplete()
			}
		}

		viewModel.imageOfDayData.observe(viewLifecycleOwner) {
			if (null != it) {
				if (it.url.isEmpty()) {
					binding.activityMainImageOfTheDayLayout.visibility = View.GONE
				} else {
					binding.activityMainImageOfTheDay.contentDescription = it.explanation
					binding.activityMainImageOfTheDayLayout.visibility = View.VISIBLE
				}
			} else
				binding.activityMainImageOfTheDayLayout.visibility = View.GONE
		}
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		inflater.inflate(R.menu.main_overflow_menu, menu)
		super.onCreateOptionsMenu(menu, inflater)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		return true
	}
}