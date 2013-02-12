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
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.core.MediaType;

import net.sf.derquinsej.io.Streams;

import com.google.common.collect.ImmutableMap;
import com.isotrol.impe3.api.FileBundleData;
import com.isotrol.impe3.api.FileData;


/**
 * FileLoader implementation support.
 * @author Andres Rodriguez.
 */
public abstract class FileLoaderSupport {
	private static final MimetypesFileTypeMap MIMEMAP = new MimetypesFileTypeMap();

	/** Not instantiable. */
	private FileLoaderSupport() {
		throw new AssertionError();
	}

	public static synchronized MediaType getMediaType(String name) {
		final String mime = MIMEMAP.getContentType(name);
		return MediaType.valueOf(mime);
	}

	/**
	 * Loads a file item contained in an zip-codified input stream.
	 * @param stream Input stream.
	 * @param name The contained file name.
	 * @return The file data or {@code null} if the file is not found.
	 * @throws NullPointerException If any of the arguments is null.
	 * @throws IOException If an error occurs.
	 */
	public static FileBundleData loadBundle(final FileData file) throws IOException {
		checkNotNull(file);
		final UUID id = file.getId();
		final String name = file.getName();
		checkArgument(file.isBundle(), "File [%s] with id [%s] is not a bundle", id, name);
		ImmutableMap.Builder<String, FileData> b = ImmutableMap.builder();
		final ZipInputStream zis = new ZipInputStream(file.getData());
		try {
			ZipEntry entry;
			while ((entry = zis.getNextEntry()) != null) {
				final byte[] data = Streams.consume(zis, false);
				final String item = entry.getName();
				b.put(item, FileData.item(file, item, getMediaType(item), data, false));
				zis.closeEntry();
			}
		}
		finally {
			zis.close();
		}
		return FileBundleData.create(file, b.build());
	}

}
