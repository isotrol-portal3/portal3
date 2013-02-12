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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

import javax.ws.rs.core.MediaType;


/**
 * Object that represents the data of a server stored file. Files are used to store binary objects used during
 * configuration. If the file is a bundle item the id is that of the bundle. <p>If the creation methods are told not to
 * copy the data is the caller's responsibility to guarantee that no modification will be made to the original byte
 * array.</p>
 * @author Andres Rodriguez.
 */
public final class FileData extends AbstractFileData {
	/** File data. */
	private final byte[] data;

	/**
	 * Creates an item file.
	 * @param bundle Bundle containing the file.
	 * @param name File name.
	 * @param mediaType Media type.
	 * @param data File data.
	 * @param copy If the constructor should perform a copy of the provided data.
	 */
	public static FileData item(FileData bundle, final String name, MediaType mediaType, byte[] data, boolean copy) {
		checkNotNull(bundle, "File bundle");
		checkArgument(bundle.isBundle(), "Not a bundle");
		return new FileData(bundle.getId(), name, mediaType, bundle.isDownloadable(), FileType.ITEM, data, copy);
	}

	/**
	 * Creates a normal or bundle file.
	 * @param id ID if the file.
	 * @param name File name.
	 * @param mediaType Media type.
	 * @param downloadable Whether the file is downloadable.
	 * @param bundle Whether the file is a bundle.
	 * @param data File data.
	 * @param copy If the constructor should perform a copy of the provided data.
	 */
	public FileData(final UUID id, final String name, MediaType mediaType, boolean downloadable, boolean bundle,
		byte[] data, boolean copy) {
		this(id, name, mediaType, downloadable, bundle ? FileType.BUNDLE : FileType.NORMAL, data, copy);
	}

	/**
	 * Constructor.
	 * @param id ID if the file.
	 * @param name File name.
	 * @param mediaType Media type.
	 * @param downloadable Whether the file is downloadable.
	 * @param type File type.
	 * @param data File data.
	 * @param copy If the constructor should perform a copy of the provided data.
	 */
	private FileData(UUID id, String name, MediaType mediaType, boolean downloadable, FileType type, byte[] data,
		boolean copy) {
		super(id, name, mediaType, downloadable, type);
		checkNotNull(data, "Data");
		if (copy) {
			this.data = new byte[data.length];
			System.arraycopy(data, 0, this.data, 0, data.length);
		} else {
			this.data = data;
		}
	}

	public boolean isBundle() {
		return getType() == FileType.BUNDLE;
	}

	public InputStream getData() {
		return new ByteArrayInputStream(data);
	}
}
