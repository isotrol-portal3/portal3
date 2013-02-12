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


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ComputationException;
import com.isotrol.impe3.api.AbstractFileData;
import com.isotrol.impe3.api.FileBundleData;
import com.isotrol.impe3.api.FileData;
import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.api.FileLoader;
import com.isotrol.impe3.api.UnableToLoadFileException;


/**
 * Abstract FileLoader implementation.
 * @author Andres Rodriguez.
 */
public abstract class AbstractFileLoader implements FileLoader {
	private static final String NOT_FOUND = "File not found";

	private final LoadingCache<UUID, AbstractFileData> map;

	/** Default constructor. */
	protected AbstractFileLoader() {
		CacheLoader<UUID, AbstractFileData> computer = new CacheLoader<UUID, AbstractFileData>() {
			public AbstractFileData load(UUID key) {
				final FileData data = doLoad(key);
				if (data.isBundle()) {
					try {
						return FileLoaderSupport.loadBundle(data);
					} catch (IOException e) {
						throw new UnableToLoadFileException(e);
					}
				}
				return data;
			}
		};
		this.map = CacheBuilder.newBuilder().softValues().expireAfterAccess(180, TimeUnit.SECONDS).build(computer);
	}

	private <T extends AbstractFileData> T load(UUID id, Class<T> type) {
		checkNotNull(id, "File id required");
		try {
			AbstractFileData afd = map.getUnchecked(id);
			checkArgument(type.isInstance(afd), "Invalid file type");
			return type.cast(afd);
		} catch (NullPointerException npe) {
			throw new IllegalArgumentException(NOT_FOUND);
		} catch (ComputationException ce) {
			final Throwable cause = ce.getCause();
			if (cause instanceof IllegalArgumentException) {
				throw (IllegalArgumentException) cause;
			} else if (cause instanceof UnableToLoadFileException) {
				throw (UnableToLoadFileException) cause;
			}
			throw new UnableToLoadFileException(cause);
		}
	}

	/**
	 * @see com.isotrol.impe3.api.FileLoader#load(com.isotrol.impe3.api.FileId)
	 */
	public final FileData load(FileId id) {
		checkNotNull(id);
		final FileData data = load(id.getId());
		checkArgument(data.getName().equals(id.getName()));
		return data;
	}

	/**
	 * @see com.isotrol.impe3.api.FileLoader#load(java.util.UUID)
	 */
	public final FileData load(UUID id) {
		return load(id, FileData.class);
	}

	/**
	 * @see com.isotrol.impe3.api.FileLoader#loadBundle(com.isotrol.impe3.api.FileId)
	 */
	public final FileBundleData loadBundle(FileId id) {
		checkNotNull(id);
		final FileBundleData data = loadBundle(id.getId());
		checkArgument(data.getName().equals(id.getName()));
		return data;
	}

	/**
	 * @see com.isotrol.impe3.api.FileLoader#loadBundle(java.util.UUID)
	 */
	public final FileBundleData loadBundle(UUID id) {
		return load(id, FileBundleData.class);
	}

	/**
	 * @see com.isotrol.impe3.api.FileLoader#loadFromBundle(com.isotrol.impe3.api.FileId, java.lang.String)
	 */
	public final FileData loadFromBundle(FileId id, String name) {
		checkNotNull(name);
		return loadBundle(id).apply(name);
	}

	/**
	 * @see com.isotrol.impe3.api.FileLoader#loadFromBundle(java.util.UUID, java.lang.String)
	 */
	public final FileData loadFromBundle(UUID id, String name) {
		checkNotNull(name);
		return loadBundle(id).apply(name);
	}

	/**
	 * Performs the actual load of a file.
	 * @param id File Id.
	 * @return The loaded file.
	 * @throws IllegalArgumentException if the file is not found.
	 * @throws UnableToLoadFileException if an exception is thrown during file loading.
	 */
	protected abstract FileData doLoad(UUID id);
}
