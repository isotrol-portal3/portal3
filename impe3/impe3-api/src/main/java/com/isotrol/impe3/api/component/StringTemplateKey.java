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

package com.isotrol.impe3.api.component;


import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Immutable string implementation of a template key link.
 * @author Andres Rodriguez
 */
public final class StringTemplateKey implements TemplateKey {
	private final String key;

	/**
	 * Gets a template key for a template name.
	 * @param key Template name.
	 * @return The template key.
	 */
	public static TemplateKey of(String key) {
		return new StringTemplateKey(key);
	}

	/**
	 * Constructor
	 * @param key Template name.
	 */
	private StringTemplateKey(String key) {
		this.key = checkNotNull(key);
	}

	@Override
	public String toString() {
		return key;
	}
}
