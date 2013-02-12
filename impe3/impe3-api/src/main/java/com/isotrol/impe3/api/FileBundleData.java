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

package com.isotrol.impe3.api;


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.ws.rs.core.MediaType;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;


/**
 * Object that represents the data of a server stored file bundle. Files are used to store binary objects used during
 * configuration. Leading prefixes "./" and "/" are removed while building the collection and ignored when querying it.
 * @author Andres Rodriguez.
 */
public final class FileBundleData extends AbstractFileData implements Function<String, FileData> {
	/** Prefixes to remove. */
	private static final ImmutableList<String> PREFIXES = ImmutableList.of("./", "/");
	/** Prefixes predicate. */
	private static final Predicate<String> STARS_WITH = new Predicate<String>() {
		public boolean apply(String input) {
			for (String prefix : PREFIXES) {
				if (input.startsWith(prefix)) {
					return true;
				}
			}
			return false;
		}
	};
	/** File map. */
	private final ImmutableMap<String, FileData> files;

	private static String clean(String name, String prefix) {
		final int n = prefix.length();
		while (name.length() > n && name.startsWith(prefix)) {
			name = name.substring(n);
		}
		return name;
	}

	private static String clean(String name) {
		checkNotNull(name, "The file name must be provided");
		for (String prefix : PREFIXES) {
			name = clean(name, prefix);
		}
		return name;
	}

	/**
	 * Constructor.
	 * @param bundle File bundle.
	 * @param name File name.
	 * @param downloadable Whether the file is downloadable.
	 * @param files Files contained in the bundle.
	 */
	public static FileBundleData create(FileData bundle, Map<String, FileData> files) {
		checkNotNull(bundle, "File bundle");
		checkArgument(bundle.isBundle(), "Not a bundle");
		return new FileBundleData(bundle.getId(), bundle.getName(), bundle.isDownloadable(), files);
	}

	/**
	 * Constructor.
	 * @param id ID if the file.
	 * @param name File name.
	 * @param downloadable Whether the file is downloadable.
	 * @param files Files contained in the bundle.
	 */
	private FileBundleData(final UUID id, final String name, boolean downloadable, Map<String, FileData> files) {
		super(id, name, MediaType.valueOf("application/zip"), downloadable, FileType.BUNDLE);
		if (Iterables.any(files.keySet(), STARS_WITH)) {
			ImmutableMap.Builder<String, FileData> b = ImmutableMap.builder();
			for (Entry<String, FileData> e : files.entrySet()) {
				b.put(clean(e.getKey()), e.getValue());
			}
			this.files = b.build();
		} else {
			this.files = ImmutableMap.copyOf(files);
		}
	}

	/**
	 * Checks whether the bundle contains a file.
	 * @param fileName File to check. Leading "/" and "./" are removed before checking.
	 * @return True if the bundle contains the file.
	 * @throws NullPointerException if the argument is {@code null}.
	 */
	public boolean contains(String fileName) {
		return files.containsKey(clean(fileName));
	}

	/**
	 * Returns the file with the requested name.
	 * @param fileName File to check. Leading "/" and "./" are removed before checking.
	 * @return The file data.
	 * @throws NullPointerException if the argument is {@code null}.
	 * @throws IllegalArgumentException if th bundle does not contain the file.
	 */
	public FileData apply(String fileName) {
		final FileData data = files.get(clean(fileName));
		if (data == null) {
			throw new IllegalArgumentException(String.format("File [%s] not found in bundle [%s] with id [%s]",
				fileName, getName(), getId()));
		}
		return data;
	}

	/**
	 * Returns a view of the collection as a map. No transformations are made to the names while querying the map.
	 * @return The map of files.
	 */
	public ImmutableMap<String, FileData> getFiles() {
		return files;
	}

	/**
	 * Returns the names of the files contained in the bundle. No transformations are made to the names while querying
	 * the set.
	 * @return The map of files.
	 */
	public ImmutableSet<String> getFileNames() {
		return files.keySet();
	}

}
