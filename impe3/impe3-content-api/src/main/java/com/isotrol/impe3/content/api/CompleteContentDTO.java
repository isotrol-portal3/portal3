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


import java.util.HashMap;
import java.util.Map;


/**
 * DTO for a complete (including blobs) content resource detail.
 * @author Andres Rodriguez Chamorro
 */
public class CompleteContentDTO extends AbstractContentDetailDTO {
	/** Serial UID. */
	private static final long serialVersionUID = 8676506430990289265L;
	/** Main blob. */
	private byte[] blob;
	/** Additional blobs. */
	private Map<String, byte[]> blobs;

	/** Default constructor. */
	public CompleteContentDTO() {
	}

	/**
	 * Returns the main blob.
	 * @return The main blob.
	 */
	public byte[] getBlob() {
		return blob;
	}

	/**
	 * Sets the main blob.
	 * @param blob The main blob.
	 */
	public void setBlob(byte[] blob) {
		this.blob = blob;
	}

	/**
	 * Returns the additional blobs in the content.
	 * @return The additional blobs in the content.
	 */
	public Map<String, byte[]> getBlobs() {
		if (blobs == null) {
			blobs = new HashMap<String, byte[]>();
		}
		return blobs;
	}

	/**
	 * Sets the additional blobs in the content.
	 * @param blobs The additional blobs in the content.
	 */
	public void setBlobs(Map<String, byte[]> blobs) {
		this.blobs = blobs;
	}
}
