/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.network.AsteroidApi
import com.udacity.asteroidradar.network.asDatabaseModel
import com.udacity.asteroidradar.network.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidsRepository(private val database: AsteroidsDatabase) {

	/**
	 * A list of asteroids that can be shown on the screen.
	 */
	val asteroids: LiveData<List<Asteroid>> = Transformations.map(
		database.asteroidDao.getAsteroids()) {
			it.asDomainModel()
		}

	/**
	 * Refresh the asteroids stored in the offline cache.
	 *
	 * This function uses the IO dispatcher to ensure the database insert database operation
	 * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
	 * function is now safe to call from any thread including the Main thread.
	 *
	 * To actually load the asteroids for use, observe [asteroids]
	 */
	suspend fun refreshAsteroids() {
		withContext(Dispatchers.IO) {
			val response = AsteroidApi.retrofitService.getAsteroids()
			val asteroids = parseAsteroidsJsonResult(JSONObject(response))
			val asteroidsDatabase = asteroids.asDatabaseModel()
			database.asteroidDao.insertAll(asteroidsDatabase)
		}
	}
}