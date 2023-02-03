package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.launch
import org.json.JSONObject

enum class ApiStatus { LOADING, ERROR, DONE }

class MainViewModel : ViewModel() {

	// The internal MutableLiveData String that stores the status of the most recent request
	private val _status = MutableLiveData<ApiStatus>()
	val status: LiveData<ApiStatus> // The external immutable LiveData for the request status String
		get() = _status

	private val _data = MutableLiveData<List<Asteroid>>()
	val data: LiveData<List<Asteroid>>
		get() = _data

	init {
		getResponse()
	}

	private fun getResponse() {
		viewModelScope.launch {
			_status.value = ApiStatus.LOADING
			try {
				val result = AsteroidApi.retrofitService.getResponse()
				if (result.isNotEmpty()) {
					_data.value = parseAsteroidsJsonResult(JSONObject(result))
				}
				_status.value = ApiStatus.DONE
			} catch (e: Exception) {
				_status.value = ApiStatus.ERROR
				_data.value = ArrayList()
			}
		}
	}
}