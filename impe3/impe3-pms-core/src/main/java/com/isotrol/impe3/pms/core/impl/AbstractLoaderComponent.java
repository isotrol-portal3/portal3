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

package com.isotrol.impe3.pms.core.impl;


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.isotrol.impe3.pms.model.EnvironmentEntity;


/**
 * Abstract loader component.
 * @author Andres Rodriguez
 */
public abstract class AbstractLoaderComponent extends AbstractService {
	/**
	 * Default constructor.
	 */
	public AbstractLoaderComponent() {
	}

	/**
	 * Loads the environment.
	 * @param envId Environment Id.
	 * @return The requested environment.
	 */
	protected final EnvironmentEntity getEnvironment(final UUID envId) {
		return findById(EnvironmentEntity.class, envId);
	}

	/**
	 * Cache for loaders.
	 * @author Andres Rodriguez
	 * @param <T> Loaded object type.
	 */
	abstract class Cache<T> implements Function<EnvironmentEntity, T> {
		private final com.google.common.cache.Cache<Key, T> cache;

		Cache(long seconds) {
			this.cache = CacheBuilder.newBuilder().softValues().expireAfterAccess(seconds, TimeUnit.SECONDS).build();
		}

		abstract int getVersion(EnvironmentEntity e);

		abstract T compute(EnvironmentEntity e);

		public final T apply(final EnvironmentEntity from) {
			final Key k = new Key(from.getId(), getVersion(from));
			final Callable<T> loader = new Callable<T>() {
				public T call() throws Exception {
					return compute(from);
				}
			};
			try {
				return cache.get(k, loader);
			} catch (ExecutionException e) {
				throw new UncheckedExecutionException(e.getCause());
			}
		}
	}

	/**
	 * Cache key for loaders.
	 */
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
			if (obj instanceof Key) {
				Key k = (Key) obj;
				return k.hash == hash && k.version == version && id.equals(k.id);
			}
			return false;
		}

	}
}
