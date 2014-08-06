/**
 * This file is part of Port@l
 * Port@l 3.0 - Portal Engine and Management System
 * Copyright (C) 2010  Isotrol, SA.  http://www.isotrol.com
 *
 * Port@l is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Port@l is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Port@l.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.isotrol.impe3.web20.impl;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;


/**
 * Abstract class for String -> Long map components.
 * @author Andres Rodriguez.
 * @param <K> Key type.
 */
public abstract class AbstractMapComponent<K> extends AbstractWeb20Service {
	private final LoadingCache<K, Long> cache;

	/** Default constructor. */
	public AbstractMapComponent() {
		final CacheLoader<K, Long> computer = new CacheLoader<K, Long>() {
			public Long load(K from) {
				return compute(from);
			}
		};
		cache = CacheBuilder.newBuilder().softValues().build(computer);
	}

	/**
	 * Computes the value of a key.
	 * @param key Key to compute.
	 * @return The computed value.
	 */
	abstract Long compute(K key);

	/**
	 * Returns the value for the provided key.
	 * @param key String key.
	 * @return The numeric value.
	 */
	final long get(K key) {
		return cache.getUnchecked(key);
	}
}
