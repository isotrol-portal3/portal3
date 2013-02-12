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

package com.isotrol.impe3.core.support;


import java.util.concurrent.ConcurrentMap;

import com.google.common.collect.MapMaker;


/**
 * Support class for storing a single value for a specified key. Null keys nor values are allowed.
 * @author Andres Rodriguez.
 * 
 * @param <K> Key type.
 * @param <V> Value type.
 */
public final class SingleValueSupport<K, V> {
	private final ConcurrentMap<K, V> map = new MapMaker().makeMap();

	/**
	 * Creates a new object. Used for type inference.
	 * @param <K> Key type.
	 * @param <V> Value type.
	 * @return The created object.
	 */
	public static final <K, V> SingleValueSupport<K, V> create() {
		return new SingleValueSupport<K, V>();
	}

	/**
	 * Default constructor.
	 */
	private SingleValueSupport() {
	}

	/**
	 * Returns the value to which the specified key is mapped, or null if this map contains no mapping for the key.
	 * @param key The key whose associated value is to be returned.
	 * @return The value to which the specified key is mapped, or null if this map contains no mapping for the key.
	 */
	public V get(K key) {
		return map.get(key);
	}

	/**
	 * If the specified key is not already associated with a value, associate it with the given value.
	 * @param key Key with which the specified value is to be associated.
	 * @param value Value to be associated with the specified key.
	 * @return The value finally associated with the specified key.
	 */
	public V put(K key, V value) {
		V previous = map.putIfAbsent(key, value);
		if (previous == null) {
			return value;
		} else {
			return previous;
		}
	}
}
