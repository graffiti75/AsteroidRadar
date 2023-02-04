package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.ImageOfDay
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.api.parseImageOfDayJsonResult
import kotlinx.coroutines.launch
import org.json.JSONObject

enum class ApiStatus { LOADING, ERROR, DONE }

class MainViewModel : ViewModel() {

	/**
	 * Asteroids
	 */

	private val _asteroidStatus = MutableLiveData<ApiStatus>()
	val asteroidStatus: LiveData<ApiStatus> // The external immutable LiveData for the request status String
		get() = _asteroidStatus

	private val _asteroidData = MutableLiveData<List<Asteroid>>()
	val asteroidData: LiveData<List<Asteroid>>
		get() = _asteroidData

	/**
	 * Image Of Day
	 */

	private val _imageOfDayStatus = MutableLiveData<ApiStatus>()
	val imageOfDayStatus: LiveData<ApiStatus> // The external immutable LiveData for the request status String
		get() = _imageOfDayStatus

	private val _imageOfDayData = MutableLiveData<ImageOfDay>()
	val imageOfDayData: LiveData<ImageOfDay>
		get() = _imageOfDayData

	init {
		getAsteroids()
		getImageOfDay()
	}

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

	private fun getImageOfDay() {
		viewModelScope.launch {
			_imageOfDayStatus.value = ApiStatus.LOADING
			try {
				val result = AsteroidApi.retrofitService.getImageOfDay()
				if (result.isNotEmpty()) {
					_imageOfDayData.value = parseImageOfDayJsonResult(JSONObject(result))
				}
				_imageOfDayStatus.value = ApiStatus.DONE
			} catch (e: Exception) {
				_imageOfDayStatus.value = ApiStatus.ERROR
				_imageOfDayData.value = ImageOfDay()
			}
		}
	}
}