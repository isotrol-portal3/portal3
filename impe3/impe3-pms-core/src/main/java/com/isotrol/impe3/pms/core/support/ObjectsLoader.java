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


import static com.google.common.base.Objects.equal;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.util.StringUtils;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.isotrol.impe3.core.Loggers;


/**
 * Offline objects loader.
 * @author Andres Rodriguez.
 * @param <T> Objects type.
 */
public final class ObjectsLoader<T> {
	@SuppressWarnings("unused")
	private final String name;
	private final LoadingCache<Key, T> cache;

	/**
	 * Creates a new object loader.
	 * @param computer Computer function.
	 * @return The created loader.
	 */
	public static <T> ObjectsLoader<T> of(final Function<UUID, T> computer) {
		return of(null, computer);
	}

	/**
	 * Creates a new object loader.
	 * @param name Object name for debugging.
	 * @param computer Computer function.
	 * @return The created loader.
	 */
	public static <T> ObjectsLoader<T> of(String name, final Function<UUID, T> computer) {
		return new ObjectsLoader<T>(name, Preconditions.checkNotNull(computer));
	}

	/**
	 * Constructor.
	 * @param name Object name for debugging.
	 * @param computer Computer function.
	 */
	private ObjectsLoader(String name, final Function<UUID, T> computer) {
		final CacheLoader<Key, T> loader = new CacheLoader<Key, T>() {
			@Override
			public T load(Key key) throws Exception {
				final Stopwatch w = new Stopwatch().start();
				try {
					return computer.apply(key.id);
				}
				finally {
					long t = w.elapsedMillis();
					if (t > 500) {
						System.out.println(String.format("State Loader [%s] took %d ms", ObjectsLoader.this.name, t));
					}
				}
			}
		};
		if (StringUtils.hasText(name)) {
			this.name = name;
		} else {
			this.name = computer.toString();
		}
		this.cache = CacheBuilder.newBuilder().maximumSize(64L).softValues()
			.expireAfterAccess(2 * 3600L, TimeUnit.SECONDS).build(loader);
	}

	/**
	 * Loads the object for the specified entry and version.
	 * @param id Entry ID.
	 * @param version Version.
	 * @return The loaded object.
	 */
	public T get(UUID id, long version) {
		return cache.getUnchecked(new Key(id, version));
	}

	/**
	 * Returns the number of requests made.
	 * @return The number of requests.
	 */
	public long getRequested() {
		return cache.stats().requestCount();
	}

	/**
	 * Returns the number of requests that had to be computed.
	 * @return The number of computed requests.
	 */
	public long getComputed() {
		return cache.stats().missCount();
	}

	/**
	 * Returns the percentage of hits that were not computed.
	 * @return The percentage of hits that were not computed.
	 */
	public double getHits() {
		return cache.stats().hitRate();
	}

	/** Cache key. */
	private static final class Key {
		private final UUID id;
		private final long version;
		private final int hash;

		Key(UUID id, long version) {
			this.id = id;
			this.version = version;
			this.hash = Objects.hashCode(id, version);
		}

		@Override
		public int hashCode() {
			return hash;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Key) {
				Key other = (Key) obj;
				return hash == other.hash && version == other.version && equal(id, other.id);
			}
			return false;
		}
	}

}
