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

package com.udacity.asteroidradar.network

import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/*
private val moshi = Moshi.Builder()
	.add(KotlinJsonAdapterFactory())
	.build()
 */

private val retrofit = Retrofit.Builder()
//	.addConverterFactory(MoshiConverterFactory.create(moshi))
	.addConverterFactory(ScalarsConverterFactory.create())
	.baseUrl(BASE_URL)
	.build()

interface ApiService {
	@GET("neo/rest/v1/feed")
	suspend fun getAsteroids(
//		@Query("start_date") startDate: String = "2022-12-31",
//		@Query("end_date") endDate: String = "2022-12-31",
		@Query("api_key") apiKey: String = API_KEY
	): String

	@GET("planetary/apod")
	suspend fun getImageOfDay(
		@Query("api_key") apiKey: String = API_KEY
	): String
}

object AsteroidApi {
	val retrofitService : ApiService by lazy {
		retrofit.create(ApiService::class.java)
	}
}