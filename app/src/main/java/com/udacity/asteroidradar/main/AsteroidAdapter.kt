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

package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.AsteroidItemBinding
import com.udacity.asteroidradar.model.Asteroid

class AsteroidAdapter(val onClickListener: OnClickListener)
	: ListAdapter<Asteroid, AsteroidAdapter.AsteroidViewHolder>(DiffCallback) {

	/**
	 * The AsteroidViewHolder constructor takes the binding variable from the associated
	 * GridViewItem, which nicely gives it access to the full [Asteroid] information.
	 */
	class AsteroidViewHolder(private var binding: AsteroidItemBinding):
		RecyclerView.ViewHolder(binding.root) {
		fun bind(asteroid: Asteroid) {
			binding.asteroid = asteroid
			binding.executePendingBindings()
		}
	}

	/**
	 * Allows the RecyclerView to determine which items have changed when the [List] of [Asteroid]
	 * has been updated.
	 */
	companion object DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
		override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
			return oldItem === newItem
		}

		override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
			return oldItem.id == newItem.id
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
		return AsteroidViewHolder(AsteroidItemBinding.inflate(LayoutInflater.from(parent.context)))
	}

	override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
		val asteroid = getItem(position)
		holder.itemView.setOnClickListener {
			onClickListener.onClick(asteroid)
		}
		holder.bind(asteroid)
	}

	/**
	 * Custom listener that handles clicks on [RecyclerView] items. Passes the [Asteroid]
	 * associated with the current item to the [onClick] function.
	 * @param clickListener lambda that will be called with the current [Asteroid]
	 */
	class OnClickListener(val clickListener: (asteroid: Asteroid) -> Unit) {
		fun onClick(asteroid: Asteroid) = clickListener(asteroid)
	}
}