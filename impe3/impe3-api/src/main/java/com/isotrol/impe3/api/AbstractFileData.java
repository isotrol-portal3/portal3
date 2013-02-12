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


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;

import javax.ws.rs.core.MediaType;


/**
 * Abstract base class for the object that represents the data of a server stored file. Files are used to store binary
 * objects used during configuration.
 * @author Andres Rodriguez.
 */
public abstract class AbstractFileData extends AbstractFile {
	/** Media type. */
	private final MediaType mediaType;
	/** Whether the file is downloadable. */
	private final boolean downloadable;
	/** File type. */
	private final FileType type;

	/**
	 * Default constructor. If the constructor is told not to copy the data is the caller's responsibility to guarantee
	 * that no modification will be made to the original byte array.
	 * @param id ID if the file.
	 * @param name File name.
	 * @param mediaType Media type.
	 * @param downloadable Whether the file is downloadable.
	 * @param type File type.
	 * @param data File data.
	 * @param copy If the constructor should perform a copy of the provided data.
	 */
	AbstractFileData(UUID id, String name, MediaType mediaType, boolean downloadable, FileType type) {
		super(id, name);
		this.mediaType = checkNotNull(mediaType, "Media type");
		this.downloadable = downloadable;
		this.type = checkNotNull(type, "File type");
	}

	/**
	 * Returns the media type.
	 * @return The media type.
	 */
	public final MediaType getMediaType() {
		return mediaType;
	}

	/**
	 * Returns whether the file is downloadable.
	 * @return True if the file is downloadable.
	 */
	public final boolean isDownloadable() {
		return downloadable;
	}

	/**
	 * Returns the file type.
	 * @return The file type.
	 */
	public final FileType getType() {
		return type;
	}

}
