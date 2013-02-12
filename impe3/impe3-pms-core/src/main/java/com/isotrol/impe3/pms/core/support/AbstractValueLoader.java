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


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;

import com.google.common.base.Objects;
import com.google.common.base.Stopwatch;
import com.google.common.cache.Cache;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.isotrol.impe3.core.Loggers;
import com.isotrol.impe3.pms.core.ValueLoader;
import com.isotrol.impe3.pms.model.WithIdVersion;


/**
 * Abstract cached instance loader.
 * @author Andres Rodriguez
 */
public abstract class AbstractValueLoader<E extends WithIdVersion, V, P> implements ValueLoader<E, V, P> {
	/** Logger. */
	private final Logger logger = Loggers.pms();
	/** Name. */
	private final String name;
	/** Cache. */
	private final Cache<Key, V> cache;

	/** Constructor. */
	protected AbstractValueLoader(String name) {
		this.name = checkNotNull(name);
		this.cache = LoaderCaches.instance();
	}
	
	/**
	 * @see com.isotrol.impe3.pms.core.ValueLoader#get(com.isotrol.impe3.pms.model.VersionedEntity, java.lang.Object)
	 */
	public final V get(E entity, P payload) {
		checkNotNull(entity);
		final Key key = new Key(entity.getId(), entity.getVersion());
		try {
			Loader loader = new Loader(entity, payload, key);
			return cache.get(key, loader);
		} catch (UncheckedExecutionException e) {
			throw new RuntimeException(e.getCause()); // TODO
		} catch (ExecutionException e) {
			throw new RuntimeException(e.getCause()); // TODO
		}
	}

	/**
	 * @see com.isotrol.impe3.pms.core.ValueLoader#get(com.isotrol.impe3.pms.model.VersionedEntity)
	 */
	public final V get(E entity) {
		return get(entity, null);
	}

	/** Loads the entity. */
	protected abstract V load(E entity, P payload);

	@Override
	public String toString() {
		return String.format("Instance Loader [%s] with [%d] elements", name, cache.size());
	}

	/** Cache key. */
	private static final class Key {
		private final UUID id;
		private final int version;
		private final int hash;

		Key(UUID id, int version) {
			this.id = checkNotNull(id);
			this.version = version;
			this.hash = Objects.hashCode(id, version);
		}

		@Override
		public int hashCode() {
			return hash;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj instanceof Key) {
				final Key k = (Key) obj;
				return hash == k.hash && version == k.version && id.equals(k.id);
			}
			return false;
		}

		@Override
		public String toString() {
			return String.format("[%s/%d]", id, version);
		}

	}

	private final class Loader implements Callable<V> {
		private final E entity;
		private final P payload;
		private final Key key;

		Loader(E entity, P payload, Key key) {
			this.entity = checkNotNull(entity);
			this.payload = payload;
			this.key = checkNotNull(key);
		}

		public V call() throws Exception {
			final Stopwatch w = new Stopwatch().start();
			try {
				return load(entity, payload);
			}
			finally {
				final long t = w.elapsedMillis();
				if (t > 150) {
					logger
						.warn(String.format("Instance Loader [%s]: Loading instance [%s] took [%d] ms", name, key, t));
				}
			}
		}
	}

}
