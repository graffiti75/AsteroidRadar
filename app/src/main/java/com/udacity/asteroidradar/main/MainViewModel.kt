package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.ImageOfDay
import com.udacity.asteroidradar.network.AsteroidApi
import com.udacity.asteroidradar.network.isNetworkConnected
import com.udacity.asteroidradar.network.parseImageOfDayJsonResult
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch
import org.json.JSONObject

enum class ApiStatus { LOADING, ERROR, DONE }

class MainViewModel(application: Application) : AndroidViewModel(application) {

	private val database = getDatabase(application)
	private val asteroidsRepository = AsteroidsRepository(database)

	/**
	 * Factory for constructing DevByteViewModel with parameter
	 */
	class Factory(val app: Application) : ViewModelProvider.Factory {
		override fun <T : ViewModel> create(modelClass: Class<T>): T {
			if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
				@Suppress("UNCHECKED_CAST")
				return MainViewModel(app) as T
			}
			throw IllegalArgumentException("Unable to construct viewmodel")
		}
	}

	/**
	 * Asteroids
	 */

	/*
	private val _asteroidStatus = MutableLiveData<ApiStatus>()
	val asteroidStatus: LiveData<ApiStatus> // The external immutable LiveData for the request status String
		get() = _asteroidStatus

	private val _asteroidData = MutableLiveData<List<Asteroid>>()
	val asteroidData: LiveData<List<Asteroid>>
		get() = _asteroidData
	 */

	/**
	 * Image Of Day
	 */

	private val _imageOfDayStatus = MutableLiveData<ApiStatus>()
	val imageOfDayStatus: LiveData<ApiStatus> // The external immutable LiveData for the request status String
		get() = _imageOfDayStatus

	private val _imageOfDayData = MutableLiveData<ImageOfDay>()
	val imageOfDayData: LiveData<ImageOfDay>
		get() = _imageOfDayData

	/**
	 * Navigate To Next Screen
	 */

	private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
	val navigateToSelectedAsteroid: LiveData<Asteroid>
		get() = _navigateToSelectedAsteroid

	init {
		val connected = application.applicationContext.isNetworkConnected()
		if (connected) {
			getImageOfDay()
			viewModelScope.launch {
				asteroidsRepository.refreshAsteroids()
			}
		}
	}
	val asteroids = asteroidsRepository.asteroids

	/*
	private fun getAsteroids() {
		viewModelScope.launch {
			_asteroidStatus.value = ApiStatus.LOADING
			try {
				val result = AsteroidApi.retrofitService.getAsteroids()
				if (result.isNotEmpty()) {
					_asteroidData.value = parseAsteroidsJsonResult(JSONObject(result))
				}
				_asteroidStatus.value = ApiStatus.DONE
			} catch (e: Exception) {
				_asteroidStatus.value = ApiStatus.ERROR
				_asteroidData.value = ArrayList()
			}
		}
	}
	*/

	private fun getImageOfDay() {
		viewModelScope.launch {
			_imageOfDayStatus.value = ApiStatus.LOADING
			try {
				val result = AsteroidApi.retrofitService.getImageOfDay()
				if (result.isNotEmpty()) {
					val parse = parseImageOfDayJsonResult(JSONObject(result))
					_imageOfDayData.value = parse
				}
				_imageOfDayStatus.value = ApiStatus.DONE
			} catch (e: Exception) {
				_imageOfDayStatus.value = ApiStatus.ERROR
				_imageOfDayData.value = ImageOfDay()
			}
		}
	}

	fun displayAsteroidDetails(asteroid: Asteroid) {
		_navigateToSelectedAsteroid.value = asteroid
	}

	fun displayAsteroidDetailsComplete() {
		_navigateToSelectedAsteroid.value = null
	}
}