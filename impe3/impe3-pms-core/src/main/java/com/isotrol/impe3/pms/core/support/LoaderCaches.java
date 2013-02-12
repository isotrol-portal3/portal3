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

package com.isotrol.impe3.pms.core.support;


import java.util.concurrent.TimeUnit;

import com.google.common.base.Function;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;


/**
 * Loaders cache support methods.
 * @author Andres Rodriguez
 */
public final class LoaderCaches {
	/** Collection maximum size. */
	private static final long COLLECTION_SIZE = 32L;
	/** Collection expiration. */
	private static final long COLLECTION_HOURS = 1L;
	/** Instance maximum size. */
	private static final long INSTANCE_SIZE = 4096L;
	/** Instance expiration. */
	private static final long INSTANCE_HOURS = 24L;
	
	/** Not instantiable. */
	private LoaderCaches() {
		throw new AssertionError();
	}

	private static CacheBuilder<Object, Object> builder(long maximumSize, long hours) {
		return CacheBuilder.newBuilder().maximumSize(maximumSize).softValues().expireAfterAccess(hours * 3600L, TimeUnit.SECONDS);
	}

	private static CacheBuilder<Object, Object> builder() {
		return builder(COLLECTION_SIZE, COLLECTION_HOURS);
	}

	public static <K, V> Cache<K, V> create(long maximumSize, long hours) {
		return builder(maximumSize, hours).build();
	}

	public static <K, V> Cache<K, V> collection() {
		return create(COLLECTION_SIZE, COLLECTION_HOURS);
	}

	public static <K, V> Cache<K, V> instance() {
		return create(INSTANCE_SIZE, INSTANCE_HOURS);
	}

	public static <K, V> LoadingCache<K, V> create(long maximumSize, long hours, CacheLoader<? super K, V> loader) {
		return builder(maximumSize, hours).build(loader);
	}

	public static <K, V> LoadingCache<K, V> collection(CacheLoader<? super K, V> loader) {
		return create(COLLECTION_SIZE, COLLECTION_HOURS, loader);
	}

	public static <K, V> LoadingCache<K, V> create(long maximumSize, long hours, Function<? super K, V> computer) {
		return builder().build(CacheLoader.from(computer));
	}

	public static <K, V> LoadingCache<K, V> collection(Function<? super K, V> computer) {
		return  create(COLLECTION_SIZE, COLLECTION_HOURS, computer);
	}
}
