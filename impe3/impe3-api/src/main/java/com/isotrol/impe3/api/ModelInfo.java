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

import java.io.Serializable;
import java.util.Calendar;
import java.util.UUID;

import com.google.common.base.Objects;


/**
 * Object representing the model creating information, including the version and creation timestamp.
 * @author Andres Rodriguez
 */
public final class ModelInfo implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = 3078410041015802372L;
	/** The model version. */
	private final UUID version;
	/** The creation timestamp. */
	private final long timestamp;

	private static long now() {
		return Calendar.getInstance().getTimeInMillis();
	}

	public static ModelInfo create(UUID version) {
		return new ModelInfo(version, now());
	}

	/**
	 * Constructor.
	 * @param version The model version.
	 * @param timestamp The creation timestamp.
	 */
	private ModelInfo(UUID version, long timestamp) {
		this.version = checkNotNull(version);
		this.timestamp = timestamp;
	}

	/**
	 * Returns the model version.
	 * @return The model version.
	 */
	public UUID getVersion() {
		return version;
	}

	/**
	 * Returns the creation timestamp.
	 * @return The creation timestamp.
	 */
	public long getTimestamp() {
		return timestamp;
	}

	public boolean isDifferentVersion(UUID version) {
		return !this.version.equals(version);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ModelInfo) {
			final ModelInfo m = (ModelInfo) obj;
			return timestamp == m.timestamp && version.equals(m.version);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(version, timestamp);
	}

}
