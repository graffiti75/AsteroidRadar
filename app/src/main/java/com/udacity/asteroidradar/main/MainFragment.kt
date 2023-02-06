package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.network.isNetworkConnected

class MainFragment : Fragment() {

	private lateinit var binding : FragmentMainBinding

	/**
	 * One way to delay creation of the viewModel until an appropriate lifecycle method is to use
	 * lazy. This requires that viewModel not be referenced before onViewCreated(), which we
	 * do in this Fragment.
	 */
	private val viewModel: MainViewModel by lazy {
		val activity = requireNotNull(this.activity) {
			"You can only access the viewModel after onViewCreated()"
		}
		ViewModelProvider(
			this,
			MainViewModel.Factory(activity.application)
		)[MainViewModel::class.java]
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

		return binding.root
	}

	private fun observeListeners() {
		val connected = requireContext().isNetworkConnected()
		if (connected) {
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
		updateConnectionStatus(connected)
	}

	private fun updateConnectionStatus(connected: Boolean) {
		if (connected) {
			binding.asteroidRecycler.visibility = View.VISIBLE
			binding.loadingSpinner.visibility = View.VISIBLE
			binding.activityMainImageOfTheDayLayout.visibility = View.VISIBLE
			binding.connectionImageView.visibility = View.GONE
		} else {
			binding.asteroidRecycler.visibility = View.GONE
			binding.loadingSpinner.visibility = View.GONE
			binding.activityMainImageOfTheDayLayout.visibility = View.GONE
			binding.connectionImageView.visibility = View.VISIBLE
		}
	}
}