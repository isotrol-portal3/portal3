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


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.isotrol.impe3.pms.core.support.TimingSupport.summary;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import net.sf.derquinsej.stats.AtomicTiming;
import net.sf.derquinsej.stats.Timings;

import org.slf4j.Logger;

import com.google.common.base.Stopwatch;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.isotrol.impe3.core.Loggers;
import com.isotrol.impe3.pms.core.CacheKey;
import com.isotrol.impe3.pms.core.GenericManager;
import com.isotrol.impe3.pms.core.support.LoaderCaches;
import com.isotrol.impe3.pms.model.EditionEntity;
import com.isotrol.impe3.pms.model.EnvironmentEntity;


/**
 * Abstract loader component.
 * @author Andres Rodriguez
 * @param <T> Loaded type.
 */
public abstract class AbstractStateLoaderComponent<T> extends AbstractService implements GenericManager<T> {
	/** Logger. */
	private final Logger logger = Loggers.pms();
	/** Cache. */
	private final LoadingCache<CacheKey, T> cache;
	/** Offline timing. */
	private final AtomicTiming offline = Timings.createAtomic(TimeUnit.MILLISECONDS);
	/** Offline load timing. */
	private final AtomicTiming offlineLoad = Timings.createAtomic(TimeUnit.MILLISECONDS);
	/** Online timing. */
	private final AtomicTiming online = Timings.createAtomic(TimeUnit.MILLISECONDS);

	/**
	 * Constructor.
	 */
	AbstractStateLoaderComponent() {
		this.cache = LoaderCaches.collection(new Computer());
	}

	/** Returns the loader to use. */
	abstract Loader<T> getLoader();

	public String getPerformanceReport() {
		return String.format("Loader [%s]: %s - %s - %s", getLoader().toString(), summary("Offline", offline.get()),
			summary("Offline Load", offlineLoad.get()), summary("Online", online.get()));
	}

	public void purge() {
		cache.invalidateAll();
	}

	public final T loadOffline(CacheKey key) {
		final Stopwatch w = Stopwatch.createStarted();
		T value = cache.getUnchecked(key);
		offline.add(w.elapsed(TimeUnit.MILLISECONDS));
		return value;
	}

	public final T loadOffline(UUID envId) {
		EnvironmentEntity environment = getEnvironment(envId);
		final CacheKey k = CacheKey.create(envId, getOfflineVersion(environment));
		return loadOffline(k);
	}

	abstract int getOfflineVersion(EnvironmentEntity e);

	public final T loadOnline(UUID envId) {
		final Stopwatch w = Stopwatch.createStarted();
		T value = getLoader().load(getEdition(envId));
		online.add(w.elapsed(TimeUnit.MILLISECONDS));
		return value;
	}

	/**
	 * Loads the environment.
	 * @param envId Environment Id.
	 * @return The requested environment.
	 */
	private EnvironmentEntity getEnvironment(UUID envId) {
		checkNotNull(envId, "The environment id must be provided");
		EnvironmentEntity e = findById(EnvironmentEntity.class, envId);
		checkArgument(e != null, "EnvironmentEntity [%s] not found", envId);
		return e;
	}

	/**
	 * Loads the environment.
	 * @param key Cache key.
	 * @return The requested environment.
	 */
	private EnvironmentEntity getEnvironment(CacheKey k) {
		checkNotNull(k, "The cache key must be provided");
		return getEnvironment(k.getId());
	}

	/**
	 * Loads an edition.
	 * @param envId Environment Id.
	 * @return The requested environment.
	 */
	private EditionEntity getEdition(UUID envId) {
		EnvironmentEntity env = getEnvironment(envId);
		EditionEntity e = env.getCurrent();
		checkArgument(e != null, "No current edition for environment [%s] not found", envId);
		return e;
	}

	/**
	 * Computer for loaders.
	 * @author Andres Rodriguez
	 * @param <T> Loaded object type.
	 */
	private final class Computer extends CacheLoader<CacheKey, T> {
		Computer() {
		}

		@Override
		public T load(CacheKey key) throws Exception {
			final Stopwatch w = Stopwatch.createStarted();
			T value = getLoader().load(getEnvironment(key));
			long t = w.elapsed(TimeUnit.MILLISECONDS);
			offlineLoad.add(t);
			if (t > 500) {
				logger.warn(String.format("Loader [%s] took [%d] ms", getLoader().toString(), t));
			}
			return value;
		}

		@Override
		public String toString() {
			return getLoader().toString();
		}
	}
}
