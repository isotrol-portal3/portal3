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

package com.isotrol.impe3.content.api;


import java.util.HashSet;
import java.util.Set;


/**
 * DTO for a content resource detail.
 * @author Andres Rodriguez Chamorro
 */
public class ContentDetailDTO extends AbstractContentDetailDTO {
	/** Serial UID. */
	private static final long serialVersionUID = 3385540050816239048L;
	/** Whether the content has a main blob. */
	private boolean blob;
	/** Blob properties. */
	private Set<String> blobs = null;

	/** Default constructor. */
	public ContentDetailDTO() {
	}

	/**
	 * Returns whether the content has a main blob.
	 * @return True if the content has a main blob.
	 */
	public boolean isBlob() {
		return blob;
	}

	/**
	 * Sets whether the content has a main blob.
	 * @param blob True if the content has a main blob.
	 */
	public void setBlob(boolean blob) {
		this.blob = blob;
	}

	/**
	 * Returns the additional blobs in the content.
	 * @return The additional blobs in the content.
	 */
	public Set<String> getBlobs() {
		if (blobs == null) {
			blobs = new HashSet<String>();
		}
		return blobs;
	}

	/**
	 * Sets the additional blobs in the content.
	 * @param blobs The additional blobs in the content.
	 */
	public void setBlobs(Set<String> blobs) {
		this.blobs = blobs;
	}
}
