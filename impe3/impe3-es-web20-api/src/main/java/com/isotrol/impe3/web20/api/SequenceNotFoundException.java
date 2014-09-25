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

package com.isotrol.impe3.web20.api;

import com.isotrol.impe3.dto.NotFoundException;

/**
 * Exception thrown when a sequence is not found by name.
 * @author Andres Rodriguez
 * @author Emilio Escobar Reyero
 */
public class SequenceNotFoundException extends NotFoundException implements OfStringId {
	/** Serial uid. */
	private static final long serialVersionUID = -2659916414234716302L;
	
	/** Sequence id. */
	private String id;

	/** Constructor. */
	public SequenceNotFoundException(String id) {
		super();
		this.id = id;
	}

	/**
	 * @see com.isotrol.impe3.web20.api.OfStringId#getStringId()
	 */
	public String getStringId() {
		return id;
	}
	
}
