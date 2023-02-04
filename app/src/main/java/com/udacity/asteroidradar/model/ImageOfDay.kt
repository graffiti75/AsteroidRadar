package com.udacity.asteroidradar.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageOfDay(
	val copyright: String = "",
	val date: String = "",
	val explanation: String = "",
	val hdurl: String = "",
	val title: String = "",
	val url: String = ""
) : Parcelable