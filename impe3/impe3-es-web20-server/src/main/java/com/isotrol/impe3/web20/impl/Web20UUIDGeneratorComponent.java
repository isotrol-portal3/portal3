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

package com.isotrol.impe3.web20.impl;


import java.util.UUID;

import net.sf.derquinsej.uuid.TimeBasedUUIDGenerator;
import net.sf.derquinsej.uuid.UUIDGenerator;

import org.springframework.stereotype.Component;


/**
 * UUID Generator to use.
 * @author Andres Rodriguez.
 */
@Component
public final class Web20UUIDGeneratorComponent {
	private final UUIDGenerator generator = new TimeBasedUUIDGenerator();

	/** Default constructor. */
	public Web20UUIDGeneratorComponent() {
	}

	/**
	 * @see com.google.common.base.Supplier#get()
	 */
	public UUID get() {
		return generator.get();
	}
}
