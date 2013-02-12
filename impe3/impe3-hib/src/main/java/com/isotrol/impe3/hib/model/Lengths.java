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

package com.isotrol.impe3.hib.model;


/**
 * Default lengths for fields.
 * @author Andres Rodriguez
 * @author Emilio Escobar Reyero
 */
public final class Lengths {
	/** UUID. */
	public static final int UUID = 36;
	/** Name. */
	public static final int NAME = 100;
	/** Title. */
	public static final int TITLE = 256;
	/** Description. */
	public static final int DESCRIPTION = 1024;
	/** Text. */
	public static final int TEXT = 2048;
	/** Long text. */
	public static final int LONGTEXT = 4096;
	/** UUID. */
	public static final int LOCALE = 16;

	/** Not instantiable. */
	private Lengths() {
		throw new AssertionError();
	}
}
