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

package com.isotrol.impe3.pms.api.user;




/**
 * DTO modifier for authorization artifacts grants.
 * @author Andres Rodriguez
 * @param <T> Artifact type.
 */
public final class Granted<T> {
	/**
	 * Creates a new modifier.
	 * @param granted True if the artifact is granted.
	 * @param artifact Artifact.
	 * @return The created modifier.
	 */
	static <T> Granted<T> of(boolean granted, T artifact) {
		return new Granted<T>(granted, artifact);
	}
	
	/** Whether the artifact is granted. */
	private boolean granted;
	/** Artifact. */
	private final T artifact;

	/**
	 * Default constructor.
	 * @param granted True if the artifact is granted.
	 * @param artifact Artifact.
	 */
	private Granted(boolean granted, T artifact) {
		this.granted = granted;
		this.artifact = artifact;
	}
	
	/**
	 * Returns whether the artifact is granted.
	 * @return True if the artifact is granted.
	 */
	public boolean isGranted() {
		return granted;
	}
	
	/**
	 * Sets whether the artifact is granted.
	 * @param active True if the artifact is granted.
	 */
	public void setGranted(boolean active) {
		this.granted = active;
	}

	/**
	 * Returns the granted.
	 * @return The granted.
	 */
	public T get() {
		return artifact;
	}
}
